package com.disaster.managementsystem.mapper;

import com.disaster.managementsystem.config.ConfigMapper;
import com.disaster.managementsystem.dto.DivisionDto;
import com.disaster.managementsystem.entity.Division;
import com.disaster.managementsystem.param.DivisionParam;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = ConfigMapper.class)
public interface DivisionMapper {
    void paramToEntity(DivisionParam param, @MappingTarget Division entity);

    DivisionDto entityToDto(Division entity);
}