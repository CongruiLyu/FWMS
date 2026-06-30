package com.fwms.pattern.strategy;

/**
 * 策略模式上下文
 */
public class AlertContext {

    private AlertStrategy strategy;

    public AlertContext(String type) {
        if ("PRODUCT".equals(type)) {
            this.strategy = new ProductAlertStrategy();
        } else {
            this.strategy = new MaterialAlertStrategy();
        }
    }

    public AlertStrategy getStrategy() {
        return strategy;
    }
}
