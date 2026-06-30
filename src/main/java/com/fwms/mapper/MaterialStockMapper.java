package com.fwms.mapper;

import com.fwms.entity.MaterialStock;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.List;

public interface MaterialStockMapper {
    List<MaterialStock> findAll();
    MaterialStock findByMaterialAndLocation(@Param("materialId") Long materialId, @Param("locationId") Long locationId);
    int insert(MaterialStock stock);
    int updateQuantity(@Param("id") Long id, @Param("quantity") BigDecimal quantity);
    int increaseStock(@Param("materialId") Long materialId, @Param("locationId") Long locationId, @Param("quantity") BigDecimal quantity);
    int decreaseStock(@Param("materialId") Long materialId, @Param("quantity") BigDecimal quantity);
}
