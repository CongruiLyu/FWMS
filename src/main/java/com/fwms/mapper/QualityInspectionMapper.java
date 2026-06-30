package com.fwms.mapper;

import com.fwms.entity.QualityInspection;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface QualityInspectionMapper {
    List<QualityInspection> findAll(@Param("type") String type);
    QualityInspection findById(@Param("id") Long id);
    int insert(QualityInspection inspection);
}
