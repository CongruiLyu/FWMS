package com.fwms.entity;

import lombok.Data;
import java.time.LocalDateTime;

/** 供应商实体，对应 supplier 表 */
@Data
public class Supplier {
    private Long id;
    private String code;
    private String name;
    private String contact;
    private String phone;
    private String address;
    private Integer status;
    private LocalDateTime createTime;
}
