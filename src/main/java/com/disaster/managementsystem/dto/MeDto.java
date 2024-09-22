package com.disaster.managementsystem.dto;

import com.disaster.managementsystem.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MeDto {
    private UUID userId;
    private String userName;
    private String email;
    private UserType userType;
    private boolean profileCreated;
    private ProfileDto profileDto;
}