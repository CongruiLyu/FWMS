package com.fwms.pattern.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 观察者模式 - 管理员通知观察者
 */
@Component
public class AdminAlertObserver implements StockObserver {

    private static final Logger log = LoggerFactory.getLogger(AdminAlertObserver.class);

    @Override
    public void onStockAlert(String type, Long refId, String refName, String message) {
        log.warn("[库存预警通知管理员] {}", message);
    }
}
