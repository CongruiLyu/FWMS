package com.fwms.mapper;

import com.fwms.entity.SalesOrder;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SalesOrderMapper {
    List<SalesOrder> findAll(@Param("status") String status);
    SalesOrder findById(@Param("id") Long id);
    int insert(SalesOrder order);
    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
