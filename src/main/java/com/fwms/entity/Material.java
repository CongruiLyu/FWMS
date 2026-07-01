package com.fwms.entity;

import lombok.Data;
import java.time.LocalDateTime;

/** 原料实体，对应 material 表，含库存预警阈值 alertThreshold */
@Data
public class Material {
    private Long id;
    private String code;
    private String name;
    private String spec;
    private String unit;
    private Integer alertThreshold;
    private Integer status;
    private LocalDateTime createTime;
}
