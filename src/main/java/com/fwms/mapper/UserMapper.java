package com.fwms.mapper;

import com.fwms.entity.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface UserMapper {
    User findByUsername(@Param("username") String username);
    User findById(@Param("id") Long id);
    List<User> findAll();
    int insert(User user);
    int update(User user);
    int deleteById(@Param("id") Long id);
}
