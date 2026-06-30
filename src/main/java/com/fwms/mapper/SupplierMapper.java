package com.fwms.mapper;

import com.fwms.entity.Supplier;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SupplierMapper {
    List<Supplier> findAll(@Param("keyword") String keyword);
    Supplier findById(@Param("id") Long id);
    int insert(Supplier supplier);
    int update(Supplier supplier);
    int deleteById(@Param("id") Long id);
}
