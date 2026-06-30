package com.fwms.enums;

public enum QcResult {
    PASS("合格"),
    FAIL("不合格"),
    REWORK("返工");

    private final String label;

    QcResult(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
