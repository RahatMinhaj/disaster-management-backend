package com.disaster.managementsystem.dto;

import com.disaster.managementsystem.dto.core.BaseDto;
import com.disaster.managementsystem.enums.Severity;
import com.disaster.managementsystem.enums.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class CrisisDto extends BaseDto {
    private String name;
    private String title;
    private String image;
    private String description;
    private boolean requiredHelp;
    private Severity severity;

    private String address;
    private DivisionDto divisionDto;
    private DistrictDto districtDto;

    private Set<ProfileDto> profileDtos;
    private Status status;
    private LocalDateTime entryTime;

}