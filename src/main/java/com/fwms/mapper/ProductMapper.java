package com.fwms.mapper;

import com.fwms.entity.Product;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ProductMapper {
    List<Product> findAll(@Param("keyword") String keyword);
    Product findById(@Param("id") Long id);
    int insert(Product product);
    int update(Product product);
    int deleteById(@Param("id") Long id);
}
