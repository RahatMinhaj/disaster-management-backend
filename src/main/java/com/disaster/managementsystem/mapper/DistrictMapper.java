package com.disaster.managementsystem.mapper;

import com.disaster.managementsystem.config.ConfigMapper;
import com.disaster.managementsystem.dto.DistrictDto;
import com.disaster.managementsystem.entity.District;
import com.disaster.managementsystem.param.DistrictParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = ConfigMapper.class)
public interface DistrictMapper {
    void paramToEntity(DistrictParam param, @MappingTarget District entity);

    @Mapping(target = "divisionDto", source = "division")
    DistrictDto entityToDto(District entity);
}