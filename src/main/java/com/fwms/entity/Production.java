package com.fwms.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Production {
    private Long id;
    private String batchNo;
    private Long productId;
    private BigDecimal quantity;
    private LocalDate productionDate;
    private Long leaderId;
    private String status;
    private LocalDateTime createTime;
    // 关联
    private String productName;
    private String leaderName;
    private String unit;
}
