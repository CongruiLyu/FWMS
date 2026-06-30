package com.fwms.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MaterialStock {
    private Long id;
    private Long materialId;
    private Long locationId;
    private BigDecimal quantity;
    private LocalDateTime lastInboundTime;
    private LocalDateTime updateTime;
    // 关联字段
    private String materialName;
    private String materialCode;
    private String unit;
    private String locationCode;
    private Integer alertThreshold;
    private Boolean alert;
}
