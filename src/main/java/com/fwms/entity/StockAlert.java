package com.fwms.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StockAlert {
    private Long id;
    private String type;
    private Long refId;
    private String refName;
    private BigDecimal currentQty;
    private Integer threshold;
    private String message;
    private Integer isRead;
    private LocalDateTime createTime;
}
