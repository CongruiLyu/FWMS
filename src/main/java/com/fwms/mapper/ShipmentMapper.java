package com.fwms.mapper;

import com.fwms.entity.Shipment;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ShipmentMapper {
    List<Shipment> findAll();
    Shipment findById(@Param("id") Long id);
    int insert(Shipment shipment);
}
