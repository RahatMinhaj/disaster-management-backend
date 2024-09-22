package com.disaster.managementsystem.dto;

import com.disaster.managementsystem.dto.core.BaseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DivisionDto extends BaseDto {
    private String name;
    private String geoCode;
    private String latitude;
    private String longitude;
}