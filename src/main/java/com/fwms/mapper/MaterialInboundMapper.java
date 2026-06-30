package com.fwms.mapper;

import com.fwms.entity.MaterialInbound;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MaterialInboundMapper {
    List<MaterialInbound> findAll();
    MaterialInbound findById(@Param("id") Long id);
    int insert(MaterialInbound inbound);
}
