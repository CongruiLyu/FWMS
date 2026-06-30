package com.fwms.mapper;

import com.fwms.entity.StockAlert;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface StockAlertMapper {
    List<StockAlert> findUnread();
    List<StockAlert> findAll();
    int insert(StockAlert alert);
    int markRead(@Param("id") Long id);
    int markAllRead();
}
