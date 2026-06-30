package com.fwms.pattern.observer;

import com.fwms.entity.StockAlert;
import com.fwms.mapper.StockAlertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式 - 库存预警主题(被观察者)
 */
@Component
public class StockAlertSubject {

    private final List<StockObserver> observers = new ArrayList<>();

    @Autowired
    private StockAlertMapper stockAlertMapper;

    public void attach(StockObserver observer) {
        observers.add(observer);
    }

    public void notifyAlert(String type, Long refId, String refName, BigDecimal currentQty, int threshold, String message) {
        StockAlert alert = new StockAlert();
        alert.setType(type);
        alert.setRefId(refId);
        alert.setRefName(refName);
        alert.setCurrentQty(currentQty);
        alert.setThreshold(threshold);
        alert.setMessage(message);
        alert.setIsRead(0);
        stockAlertMapper.insert(alert);

        for (StockObserver observer : observers) {
            observer.onStockAlert(type, refId, refName, message);
        }
    }
}
