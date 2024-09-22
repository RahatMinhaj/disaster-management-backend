package com.disaster.managementsystem.mapper;

import com.disaster.managementsystem.config.ConfigMapper;
import com.disaster.managementsystem.dto.ProfileDto;
import com.disaster.managementsystem.entity.Profile;
import com.disaster.managementsystem.param.ProfileParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(config = ConfigMapper.class)
public interface ProfileMapper {
    void paramToEntity(ProfileParam param, @MappingTarget Profile entity);

    @Mappings({
            @Mapping(target = "userDto",source = "user")
    })
    ProfileDto entityToDto(Profile entity);
}