package com.fwms.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseOrder {
    private Long id;
    private String orderNo;
    private Long supplierId;
    private LocalDate purchaseDate;
    private LocalDate expectedDate;
    private BigDecimal totalAmount;
    private String status;
    private Long purchaserId;
    private String remark;
    private LocalDateTime createTime;
    // 关联
    private String supplierName;
    private String purchaserName;
    private List<PurchaseOrderItem> items;
}
