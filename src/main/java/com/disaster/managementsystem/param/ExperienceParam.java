package com.disaster.managementsystem.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceParam extends BaseParam {
    @Schema(hidden = true)
    private UUID id;
    private String companyName;
    private String department;
    private String position;
    private double totalExperience;
    private String workSummary;
}
