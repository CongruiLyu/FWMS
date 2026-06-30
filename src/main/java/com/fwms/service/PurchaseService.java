package com.fwms.service;

import com.fwms.common.OrderNoGenerator;
import com.fwms.entity.PurchaseOrder;
import com.fwms.entity.PurchaseOrderItem;
import com.fwms.enums.OrderStatus;
import com.fwms.mapper.PurchaseOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    public List<PurchaseOrder> findAll(String status) {
        List<PurchaseOrder> orders = purchaseOrderMapper.findAll(status);
        for (PurchaseOrder order : orders) {
            order.setItems(purchaseOrderMapper.findItemsByOrderId(order.getId()));
        }
        return orders;
    }

    public PurchaseOrder findById(Long id) {
        PurchaseOrder order = purchaseOrderMapper.findById(id);
        if (order != null) {
            order.setItems(purchaseOrderMapper.findItemsByOrderId(id));
        }
        return order;
    }

    @Transactional
    public PurchaseOrder create(PurchaseOrder order, List<PurchaseOrderItem> items) {
        order.setOrderNo(OrderNoGenerator.generate("PO"));
        order.setStatus(OrderStatus.PENDING.name());
        BigDecimal total = BigDecimal.ZERO;
        for (PurchaseOrderItem item : items) {
            if (item.getUnitPrice() != null) {
                total = total.add(item.getUnitPrice().multiply(item.getQuantity()));
            }
        }
        order.setTotalAmount(total);
        purchaseOrderMapper.insert(order);
        for (PurchaseOrderItem item : items) {
            item.setOrderId(order.getId());
            purchaseOrderMapper.insertItem(item);
        }
        order.setItems(items);
        return order;
    }

    public void markArrived(Long id) {
        purchaseOrderMapper.updateStatus(id, OrderStatus.ARRIVED.name());
    }
}
