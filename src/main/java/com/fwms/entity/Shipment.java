package com.fwms.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Shipment {
    private Long id;
    private String shipmentNo;
    private Long salesOrderId;
    private Long productId;
    private BigDecimal quantity;
    private Long locationId;
    private Long operatorId;
    private LocalDateTime shipmentTime;
    private String remark;
    // 关联
    private String productName;
    private String locationCode;
    private String operatorName;
    private String orderNo;
    private String customerName;
}
