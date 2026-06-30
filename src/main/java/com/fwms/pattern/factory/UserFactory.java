package com.fwms.pattern.factory;

import com.fwms.entity.User;
import com.fwms.enums.UserRole;

/**
 * 工厂模式 - 根据角色创建不同权限的用户对象
 */
public class UserFactory {

    public static User createUser(String role, String username, String password, String realName) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRealName(realName);
        user.setRole(role);
        user.setStatus(1);
        return user;
    }

    public static String getRoleLabel(String role) {
        try {
            return UserRole.fromCode(role).getLabel();
        } catch (Exception e) {
            return role;
        }
    }

    public static boolean hasPermission(String role, String module) {
        if ("ADMIN".equals(role)) {
            return true;
        }
        switch (module) {
            case "supplier":
            case "purchase":
                return "PURCHASER".equals(role);
            case "material":
            case "inbound":
            case "outbound":
            case "location":
            case "shipment":
                return "WAREHOUSE".equals(role);
            case "qc":
                return "QC".equals(role);
            case "production":
                return "PRODUCTION".equals(role);
            case "requisition":
                return "PRODUCTION".equals(role) || "WAREHOUSE".equals(role);
            case "customer":
            case "sales":
                return "SALES".equals(role);
            case "dashboard":
            case "stock":
                return true;
            default:
                return false;
        }
    }
}
