package com.disaster.managementsystem.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@SuperBuilder
public class PoliceStationDto {
    private DistrictDto districtDto;
    private String name;
    private String geoCode;
    private String latitude;
    private String longitude;
}