package com.disaster.managementsystem.mapper;

import com.disaster.managementsystem.config.ConfigMapper;
import com.disaster.managementsystem.dto.InventoryDto;
import com.disaster.managementsystem.entity.Inventory;
import com.disaster.managementsystem.param.InventoryParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = ConfigMapper.class, uses = {ProfileMapper.class})
public interface InventoryMapper {
    void paramToEntity(InventoryParam param, @MappingTarget Inventory entity);

    @Mapping(source = "profile", target = "profileDto")
    InventoryDto entityToDto(Inventory inventory);
}
