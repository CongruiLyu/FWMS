package com.fwms.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MaterialInbound {
    private Long id;
    private String inboundNo;
    private Long purchaseOrderId;
    private Long supplierId;
    private Long materialId;
    private BigDecimal quantity;
    private Long locationId;
    private String qcResult;
    private Long qcInspectorId;
    private Long operatorId;
    private LocalDateTime inboundTime;
    private String remark;
    // 关联
    private String supplierName;
    private String materialName;
    private String locationCode;
    private String inspectorName;
}
