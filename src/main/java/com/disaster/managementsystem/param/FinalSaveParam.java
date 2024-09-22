package com.disaster.managementsystem.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FinalSaveParam {
    private String name;
    @Builder.Default
    private Boolean active = Boolean.TRUE;
}
