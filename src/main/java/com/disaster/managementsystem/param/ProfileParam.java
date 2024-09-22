package com.disaster.managementsystem.param;

import com.disaster.managementsystem.enums.GenderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileParam extends BaseParam{
    @Schema(hidden = true)
    private UUID id;
    private String name;
    private String mobileNo;
    private String address;
    private GenderType genderType;
    private LocalDate dateOfBirth;
    private String photoFilePath;




    @NotNull
    private UUID userId;
}