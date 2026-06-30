package com.fwms.mapper;

import com.fwms.entity.MaterialOutbound;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MaterialOutboundMapper {
    List<MaterialOutbound> findAll(@Param("status") String status);
    MaterialOutbound findById(@Param("id") Long id);
    int insert(MaterialOutbound outbound);
    int updateStatus(@Param("id") Long id, @Param("status") String status, @Param("approverId") Long approverId);
}
