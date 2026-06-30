package com.fwms.enums;

public enum UserRole {
    ADMIN("管理员"),
    WAREHOUSE("仓库管理员"),
    PURCHASER("采购员"),
    QC("质检员"),
    PRODUCTION("生产人员"),
    SALES("销售员");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static UserRole fromCode(String code) {
        return UserRole.valueOf(code);
    }
}
