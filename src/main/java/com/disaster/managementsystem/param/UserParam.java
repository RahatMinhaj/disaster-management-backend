package com.disaster.managementsystem.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserParam {
    @Schema(hidden = true)
    private UUID id;
    private String userName;
    private String password;
    private String email;
}