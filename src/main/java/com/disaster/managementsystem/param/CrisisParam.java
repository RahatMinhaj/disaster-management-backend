package com.disaster.managementsystem.param;

import com.disaster.managementsystem.enums.Severity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrisisParam extends BaseParam {
    @Schema(hidden = true)
    private UUID id;
    private String name;
    private String title;
    private String image;
    private String description;
    private boolean requiredHelp;
    private Severity severity;
    private String location;

    private String address;
    private UUID divisionId;
    private UUID districtId;
    private UUID policeStationId;

}
