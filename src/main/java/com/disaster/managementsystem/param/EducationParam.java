package com.disaster.managementsystem.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationParam extends BaseParam {
    @Schema(hidden = true)
    private UUID id;
    private String degreeName;
    private String instituteName;
    private int passingYear;
    private double result;
}
