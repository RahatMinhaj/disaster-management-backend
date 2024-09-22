package com.disaster.managementsystem.dto;

import com.disaster.managementsystem.dto.core.BaseDto;
import com.disaster.managementsystem.enums.UserType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class UserDto extends BaseDto {
    private String userName;
    private String email;
    private UserType userType;
    private boolean profileCreated;
}