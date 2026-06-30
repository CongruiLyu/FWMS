package com.fwms.mapper;

import com.fwms.entity.Material;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MaterialMapper {
    List<Material> findAll(@Param("keyword") String keyword);
    Material findById(@Param("id") Long id);
    int insert(Material material);
    int update(Material material);
    int deleteById(@Param("id") Long id);
}
