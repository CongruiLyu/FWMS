package com.fwms.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PurchaseOrderItem {
    private Long id;
    private Long orderId;
    private Long materialId;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    // 关联
    private String materialName;
    private String materialCode;
    private String unit;
}
