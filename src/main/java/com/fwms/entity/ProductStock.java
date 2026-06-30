package com.fwms.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProductStock {
    private Long id;
    private Long productId;
    private Long locationId;
    private BigDecimal quantity;
    private LocalDate productionDate;
    private LocalDate expiryDate;
    private LocalDate deliveryDate;
    private LocalDateTime lastInboundTime;
    private LocalDateTime updateTime;
    // 关联
    private String productName;
    private String productCode;
    private String unit;
    private String locationCode;
    private Integer alertThreshold;
    private Boolean alert;
}
