package com.disaster.managementsystem.dto;

import com.disaster.managementsystem.dto.core.BaseDto;
import com.disaster.managementsystem.enums.GenderType;
import com.disaster.managementsystem.enums.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class ProfileDto extends BaseDto {
    private String name;
    private String mobileNo;
    private String address;
    private GenderType genderType;
    private LocalDate dateOfBirth;
    private UserDto userDto;
    private Status status;
}