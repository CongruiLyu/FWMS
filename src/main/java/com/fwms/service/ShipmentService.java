package com.fwms.service;

import com.fwms.common.OrderNoGenerator;
import com.fwms.entity.*;
import com.fwms.enums.OrderStatus;
import com.fwms.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentMapper shipmentMapper;
    @Autowired
    private SalesOrderMapper salesOrderMapper;
    @Autowired
    private ProductStockMapper productStockMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private DashboardService dashboardService;

    public List<Shipment> findAll() {
        return shipmentMapper.findAll();
    }

    public List<SalesOrder> findPendingOrders() {
        return salesOrderMapper.findAll("PENDING");
    }

    /**
     * 发货出库流程：拣货 -> 出库 -> 库存减少
     */
    @Transactional
    public Shipment ship(Long salesOrderId, Long locationId, Long operatorId) {
        SalesOrder order = salesOrderMapper.findById(salesOrderId);
        if (order == null) {
            throw new RuntimeException("销售订单不存在");
        }
        if (!OrderStatus.PENDING.name().equals(order.getStatus()) && !OrderStatus.PICKING.name().equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许发货");
        }

        BigDecimal totalStock = productStockMapper.getTotalQuantity(order.getProductId());
        if (totalStock.compareTo(order.getQuantity()) < 0) {
            throw new RuntimeException("成品库存不足，无法发货");
        }

        salesOrderMapper.updateStatus(salesOrderId, OrderStatus.PICKING.name());

        int rows = productStockMapper.decreaseStock(order.getProductId(), order.getQuantity());
        if (rows == 0) {
            throw new RuntimeException("库存扣减失败");
        }

        Shipment shipment = new Shipment();
        shipment.setShipmentNo(OrderNoGenerator.generate("SH"));
        shipment.setSalesOrderId(salesOrderId);
        shipment.setProductId(order.getProductId());
        shipment.setQuantity(order.getQuantity());
        shipment.setLocationId(locationId);
        shipment.setOperatorId(operatorId);
        shipmentMapper.insert(shipment);

        salesOrderMapper.updateStatus(salesOrderId, OrderStatus.SHIPPED.name());

        Product product = productMapper.findById(order.getProductId());
        if (product != null) {
            BigDecimal remainQty = productStockMapper.getTotalQuantity(order.getProductId());
            dashboardService.checkAndAlertProduct(product.getId(), product.getName(),
                    remainQty, product.getAlertThreshold(), product.getUnit());
        }

        return shipment;
    }
}
