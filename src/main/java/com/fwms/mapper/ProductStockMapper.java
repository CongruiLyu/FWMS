package com.fwms.mapper;

import com.fwms.entity.ProductStock;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.List;

public interface ProductStockMapper {
    List<ProductStock> findAll();
    ProductStock findByProductAndLocation(@Param("productId") Long productId, @Param("locationId") Long locationId);
    int insert(ProductStock stock);
    int increaseStock(@Param("productId") Long productId, @Param("locationId") Long locationId, @Param("quantity") BigDecimal quantity);
    int decreaseStock(@Param("productId") Long productId, @Param("quantity") BigDecimal quantity);
    BigDecimal getTotalQuantity(@Param("productId") Long productId);
}
