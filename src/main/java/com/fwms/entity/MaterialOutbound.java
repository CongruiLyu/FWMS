package com.fwms.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MaterialOutbound {
    private Long id;
    private String outboundNo;
    private String workshop;
    private Long applicantId;
    private Long materialId;
    private BigDecimal quantity;
    private String status;
    private Long approverId;
    private LocalDateTime outboundTime;
    private LocalDateTime createTime;
    private String remark;
    // 关联
    private String materialName;
    private String applicantName;
    private String approverName;
    private String unit;
}
