package com.fwms.mapper;

import com.fwms.entity.Customer;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface CustomerMapper {
    List<Customer> findAll(@Param("keyword") String keyword);
    Customer findById(@Param("id") Long id);
    int insert(Customer customer);
    int update(Customer customer);
    int deleteById(@Param("id") Long id);
}
