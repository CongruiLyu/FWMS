package com.fwms.service;

import com.fwms.entity.*;
import com.fwms.mapper.*;
import com.fwms.pattern.observer.AdminAlertObserver;
import com.fwms.pattern.observer.StockAlertSubject;
import com.fwms.pattern.strategy.AlertContext;
import com.fwms.pattern.strategy.AlertStrategy;
import com.fwms.common.OrderNoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DashboardService {

    @Autowired
    private MaterialStockMapper materialStockMapper;
    @Autowired
    private ProductStockMapper productStockMapper;
    @Autowired
    private StockAlertMapper stockAlertMapper;
    @Autowired
    private WarehouseLocationMapper locationMapper;
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    private SalesOrderMapper salesOrderMapper;
    @Autowired
    private MaterialInboundMapper inboundMapper;
    @Autowired
    private ShipmentMapper shipmentMapper;
    @Autowired
    private StockAlertSubject alertSubject;
    @Autowired
    private AdminAlertObserver adminObserver;

    @PostConstruct
    public void init() {
        alertSubject.attach(adminObserver);
    }

    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        List<MaterialStock> materialStocks = materialStockMapper.findAll();
        List<ProductStock> productStocks = productStockMapper.findAll();
        List<StockAlert> alerts = stockAlertMapper.findUnread();
        List<WarehouseLocation> locations = locationMapper.findAll();

        data.put("materialStocks", materialStocks);
        data.put("productStocks", productStocks);
        data.put("alerts", alerts);
        data.put("locations", locations);
        data.put("warehouseVisual", buildWarehouseVisual(locations, materialStocks, productStocks));
        data.put("pendingPurchases", purchaseOrderMapper.findAll("PENDING").size());
        data.put("pendingSales", salesOrderMapper.findAll("PENDING").size());
        data.put("todayInbound", inboundMapper.findAll().stream()
                .filter(i -> i.getInboundTime() != null && i.getInboundTime().toLocalDate().equals(LocalDate.now())).count());
        data.put("todayShipment", shipmentMapper.findAll().stream()
                .filter(s -> s.getShipmentTime() != null && s.getShipmentTime().toLocalDate().equals(LocalDate.now())).count());
        return data;
    }

    private List<Map<String, Object>> buildWarehouseVisual(List<WarehouseLocation> locations,
            List<MaterialStock> materialStocks, List<ProductStock> productStocks) {
        List<Map<String, Object>> visual = new ArrayList<>();
        for (WarehouseLocation loc : locations) {
            Map<String, Object> item = new HashMap<>();
            item.put("code", loc.getCode());
            item.put("name", loc.getName());
            item.put("zone", loc.getZone());
            item.put("zoneName", loc.getZoneName());
            BigDecimal qty = BigDecimal.ZERO;
            String itemName = "";
            int maxCapacity = loc.getCapacity() != null && loc.getCapacity() > 0 ? loc.getCapacity() : 500;

            for (MaterialStock ms : materialStocks) {
                if (loc.getId().equals(ms.getLocationId())) {
                    qty = ms.getQuantity();
                    itemName = ms.getMaterialName();
                    break;
                }
            }
            for (ProductStock ps : productStocks) {
                if (loc.getId().equals(ps.getLocationId())) {
                    qty = ps.getQuantity();
                    itemName = ps.getProductName();
                    break;
                }
            }
            item.put("itemName", itemName);
            item.put("quantity", qty);
            double percent = maxCapacity > 0 ? Math.min(100, qty.doubleValue() / maxCapacity * 100) : 0;
            item.put("fillPercent", percent);
            visual.add(item);
        }
        return visual;
    }

    public void checkAndAlertMaterial(Long materialId, String materialName, BigDecimal qty, int threshold, String unit) {
        AlertStrategy strategy = new AlertContext("MATERIAL").getStrategy();
        if (strategy.isAlert(qty, threshold)) {
            String msg = strategy.getAlertMessage(materialName, qty, threshold, unit);
            alertSubject.notifyAlert("MATERIAL", materialId, materialName, qty, threshold, msg);
        }
    }

    public void checkAndAlertProduct(Long productId, String productName, BigDecimal qty, int threshold, String unit) {
        AlertStrategy strategy = new AlertContext("PRODUCT").getStrategy();
        if (strategy.isAlert(qty, threshold)) {
            String msg = strategy.getAlertMessage(productName, qty, threshold, unit);
            alertSubject.notifyAlert("PRODUCT", productId, productName, qty, threshold, msg);
        }
    }
}
