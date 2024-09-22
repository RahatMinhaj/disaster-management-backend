package com.disaster.managementsystem.dto.core;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Data
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@SuperBuilder(toBuilder = true)
public class BaseDto implements Serializable {
    private UUID id;
    @Builder.Default
    private Long version = 0L;
    @Builder.Default
    private Boolean deleted = Boolean.FALSE;
    @Builder.Default
    private Boolean active = Boolean.TRUE;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private String ipAddress;
}
