package com.fwms.entity;

import lombok.Data;
import java.time.LocalDateTime;

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
