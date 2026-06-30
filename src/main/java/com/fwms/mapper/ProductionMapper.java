package com.fwms.mapper;

import com.fwms.entity.Production;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ProductionMapper {
    List<Production> findAll(@Param("status") String status);
    Production findById(@Param("id") Long id);
    int insert(Production production);
    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
