package com.fwms.service;

import com.fwms.common.OrderNoGenerator;
import com.fwms.entity.*;
import com.fwms.enums.OrderStatus;
import com.fwms.enums.QcResult;
import com.fwms.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductionService {

    @Autowired
    private ProductionMapper productionMapper;
    @Autowired
    private QualityInspectionMapper qcMapper;
    @Autowired
    private ProductStockMapper productStockMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private WarehouseLocationMapper locationMapper;
    @Autowired
    private DashboardService dashboardService;

    public List<Production> findAll(String status) {
        return productionMapper.findAll(status);
    }

    public Production create(Production production) {
        production.setBatchNo(OrderNoGenerator.generate("PB"));
        production.setStatus(OrderStatus.PRODUCING.name());
        productionMapper.insert(production);
        return production;
    }

    public void submitForQc(Long id) {
        productionMapper.updateStatus(id, OrderStatus.QC_PENDING.name());
    }

    @Transactional
    public void productQc(Long productionId, Long inspectorId, String result, String remark) {
        Production production = productionMapper.findById(productionId);
        if (production == null) {
            throw new RuntimeException("生产批次不存在");
        }

        QualityInspection qc = new QualityInspection();
        qc.setInspectionNo(OrderNoGenerator.generate("QC"));
        qc.setType("PRODUCT");
        qc.setRefId(productionId);
        qc.setProductId(production.getProductId());
        qc.setQuantity(production.getQuantity());
        qc.setResult(result);
        qc.setInspectorId(inspectorId);
        qc.setRemark(remark);
        qcMapper.insert(qc);

        if (QcResult.FAIL.name().equals(result)) {
            productionMapper.updateStatus(productionId, OrderStatus.QC_FAIL.name());
        } else if (QcResult.REWORK.name().equals(result)) {
            productionMapper.updateStatus(productionId, OrderStatus.PRODUCING.name());
        } else {
            productionMapper.updateStatus(productionId, OrderStatus.QC_PASS.name());
        }
    }

    /**
     * 成品入库 - 按交货期安排库位
     */
    @Transactional
    public ProductStock storeProduct(Long productionId, Long locationId, LocalDate deliveryDate) {
        Production production = productionMapper.findById(productionId);
        if (production == null || !OrderStatus.QC_PASS.name().equals(production.getStatus())) {
            throw new RuntimeException("生产批次未通过质检，无法入库");
        }

        Product product = productMapper.findById(production.getProductId());
        LocalDate prodDate = production.getProductionDate();
        LocalDate expiryDate = prodDate.plusDays(product.getShelfLifeDays());

        ProductStock existing = productStockMapper.findByProductAndLocation(production.getProductId(), locationId);
        if (existing != null) {
            productStockMapper.increaseStock(production.getProductId(), locationId, production.getQuantity());
            existing.setQuantity(existing.getQuantity().add(production.getQuantity()));
        } else {
            ProductStock stock = new ProductStock();
            stock.setProductId(production.getProductId());
            stock.setLocationId(locationId);
            stock.setQuantity(production.getQuantity());
            stock.setProductionDate(prodDate);
            stock.setExpiryDate(expiryDate);
            stock.setDeliveryDate(deliveryDate != null ? deliveryDate : LocalDate.now());
            stock.setLastInboundTime(LocalDateTime.now());
            productStockMapper.insert(stock);
            existing = stock;
        }

        productionMapper.updateStatus(productionId, OrderStatus.STORED.name());

        BigDecimal totalQty = productStockMapper.getTotalQuantity(production.getProductId());
        dashboardService.checkAndAlertProduct(product.getId(), product.getName(),
                totalQty, product.getAlertThreshold(), product.getUnit());

        return existing;
    }
}
