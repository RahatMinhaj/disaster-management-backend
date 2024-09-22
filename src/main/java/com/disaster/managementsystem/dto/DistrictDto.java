package com.disaster.managementsystem.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@SuperBuilder
public class DistrictDto {
    private String name;
    private String geoCode;
    private String latitude;
    private String longitude;
    private DivisionDto divisionDto;

}