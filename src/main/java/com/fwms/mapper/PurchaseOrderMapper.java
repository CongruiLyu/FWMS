package com.fwms.mapper;

import com.fwms.entity.PurchaseOrder;
import com.fwms.entity.PurchaseOrderItem;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface PurchaseOrderMapper {
    List<PurchaseOrder> findAll(@Param("status") String status);
    PurchaseOrder findById(@Param("id") Long id);
    int insert(PurchaseOrder order);
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    int insertItem(PurchaseOrderItem item);
    List<PurchaseOrderItem> findItemsByOrderId(@Param("orderId") Long orderId);
}
