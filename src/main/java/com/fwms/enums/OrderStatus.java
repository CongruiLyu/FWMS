package com.fwms.enums;

public enum OrderStatus {
    PENDING("待处理"),
    ARRIVED("已到货"),
    APPROVED("已审核"),
    REJECTED("已拒绝"),
    COMPLETED("已完成"),
    CANCELLED("已取消"),
    PICKING("拣货中"),
    SHIPPED("已发货"),
    PRODUCING("生产中"),
    QC_PENDING("待质检"),
    QC_PASS("质检通过"),
    QC_FAIL("质检不合格"),
    STORED("已入库");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
