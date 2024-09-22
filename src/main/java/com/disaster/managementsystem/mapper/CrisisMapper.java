package com.disaster.managementsystem.mapper;

import com.disaster.managementsystem.config.ConfigMapper;
import com.disaster.managementsystem.dto.CrisisDto;
import com.disaster.managementsystem.entity.Crisis;
import com.disaster.managementsystem.param.CrisisParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = ConfigMapper.class, uses = {ProfileMapper.class})
public interface CrisisMapper {
    void paramToEntity(CrisisParam param, @MappingTarget Crisis entity);

    @Mapping(target = "profileDtos", source = "profiles")
    CrisisDto entityToDto(Crisis crisis);
}