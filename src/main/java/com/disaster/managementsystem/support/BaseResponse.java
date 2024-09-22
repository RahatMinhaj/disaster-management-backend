package com.disaster.managementsystem.support;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"nonce","status","message"})
@ToString(of = {"nonce","status","message"})
@SuperBuilder(toBuilder = true)
public abstract class BaseResponse {
    private Long nonce;
    private int status;
    private String message;
    private ErrorDto error;
}
