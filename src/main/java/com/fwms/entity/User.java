package com.fwms.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体，对应 user 表，支持 ADMIN/WAREHOUSE/PURCHASER/QC/PRODUCTION/SALES 六种角色
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String role;
    private String phone;
    private Integer status;
    private LocalDateTime createTime;
}
