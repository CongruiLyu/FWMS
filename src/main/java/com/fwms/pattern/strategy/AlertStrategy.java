package com.fwms.pattern.strategy;

import java.math.BigDecimal;

/**
 * 策略模式 - 不同物料/产品使用不同预警策略
 */
public interface AlertStrategy {
    boolean isAlert(BigDecimal currentQty, int threshold);
    String getAlertMessage(String name, BigDecimal currentQty, int threshold, String unit);
}
