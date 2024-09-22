package com.disaster.managementsystem.dto;

import com.disaster.managementsystem.dto.core.BaseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class ProductCategoryDto extends BaseDto {
    private String name;
    private String description;
}
