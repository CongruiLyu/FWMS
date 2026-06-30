package com.fwms.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;
    private String code;
    private String name;
    private String spec;
    private String unit;
    private Integer shelfLifeDays;
    private Integer alertThreshold;
    private Integer status;
    private LocalDateTime createTime;
}
