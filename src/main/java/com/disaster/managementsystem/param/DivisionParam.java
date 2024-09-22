package com.disaster.managementsystem.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DivisionParam extends BaseParam {
    @Schema(hidden = true)
    private UUID id;
    private String name;
    private String geoCode;
    private String latitude;
    private String longitude;
}
