package com.fwms.mapper;

import com.fwms.entity.WarehouseLocation;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface WarehouseLocationMapper {
    List<WarehouseLocation> findAll();
    List<WarehouseLocation> findByZone(@Param("zone") String zone);
    WarehouseLocation findById(@Param("id") Long id);
    int insert(WarehouseLocation location);
    int update(WarehouseLocation location);
    int deleteById(@Param("id") Long id);
}
