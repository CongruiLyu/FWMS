package com.fwms.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SalesOrder {
    private Long id;
    private String orderNo;
    private Long customerId;
    private Long productId;
    private BigDecimal quantity;
    private LocalDate deliveryDate;
    private String status;
    private Long salespersonId;
    private LocalDateTime createTime;
    private String remark;
    // 关联
    private String customerName;
    private String productName;
    private String salespersonName;
    private String unit;
}
