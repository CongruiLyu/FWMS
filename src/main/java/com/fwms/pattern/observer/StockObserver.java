package com.fwms.pattern.observer;

/**
 * 观察者模式 - 库存预警观察者接口
 */
public interface StockObserver {
    void onStockAlert(String type, Long refId, String refName, String message);
}
