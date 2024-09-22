package com.disaster.managementsystem.support;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class DeleteResponseDto extends BaseResponse {
    private boolean deleted;
}
