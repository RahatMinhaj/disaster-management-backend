package com.disaster.managementsystem.mapper;

import com.disaster.managementsystem.config.ConfigMapper;
import com.disaster.managementsystem.dto.PoliceStationDto;
import com.disaster.managementsystem.entity.PoliceStation;
import com.disaster.managementsystem.param.PoliceStationParam;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = ConfigMapper.class)
public interface PoliceStationMapper {
    void paramToEntity(PoliceStationParam param, @MappingTarget PoliceStation entity);

    PoliceStationDto entityToDto(PoliceStation entity);
}