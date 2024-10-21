package com.disaster.managementsystem.mapper;

import com.disaster.managementsystem.config.ConfigMapper;
import com.disaster.managementsystem.dto.UserDto;
import com.disaster.managementsystem.entity.User;
import com.disaster.managementsystem.param.UserParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = ConfigMapper.class)
public interface UserMapper {
    void paramToEntity(UserParam param, @MappingTarget User entity);

    @Mapping(target = "userName", source = "username")
    UserDto entityToDto(User user);
}