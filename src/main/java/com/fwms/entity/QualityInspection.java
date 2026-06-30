package com.fwms.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class QualityInspection {
    private Long id;
    private String inspectionNo;
    private String type;
    private Long refId;
    private Long materialId;
    private Long productId;
    private BigDecimal quantity;
    private String result;
    private Long inspectorId;
    private LocalDateTime inspectTime;
    private String remark;
    // 关联
    private String materialName;
    private String productName;
    private String inspectorName;
}
