package com.fwms.pattern.strategy;

import java.math.BigDecimal;

public class ProductAlertStrategy implements AlertStrategy {

    @Override
    public boolean isAlert(BigDecimal currentQty, int threshold) {
        return currentQty.compareTo(BigDecimal.valueOf(threshold)) <= 0;
    }

    @Override
    public String getAlertMessage(String name, BigDecimal currentQty, int threshold, String unit) {
        return "⚠ 成品" + name + "库存不足，当前" + currentQty.stripTrailingZeros().toPlainString()
                + unit + "，预警阈值" + threshold + unit;
    }
}
