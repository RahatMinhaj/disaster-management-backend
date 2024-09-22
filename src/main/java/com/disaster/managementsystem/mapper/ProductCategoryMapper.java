package com.disaster.managementsystem.mapper;

import com.disaster.managementsystem.config.ConfigMapper;
import com.disaster.managementsystem.dto.ProductCategoryDto;
import com.disaster.managementsystem.entity.ProductCategory;
import com.disaster.managementsystem.param.ProductCategoryParam;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = ConfigMapper.class)
public interface ProductCategoryMapper {
    void paramToEntity(ProductCategoryParam param, @MappingTarget ProductCategory entity);

    ProductCategoryDto entityToDto(ProductCategory organization);

}
