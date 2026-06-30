package com.fwms.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WarehouseLocation {
    private Long id;
    private String code;
    private String name;
    private String zone;
    private String zoneName;
    private Integer capacity;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
}
