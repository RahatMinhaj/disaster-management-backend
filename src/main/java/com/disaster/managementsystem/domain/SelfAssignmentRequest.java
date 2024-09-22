package com.disaster.managementsystem.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.disaster.managementsystem.entity.Profile;
import com.disaster.managementsystem.enums.Status;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@ToString
@Builder
public class SelfAssignmentRequest {

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    @JsonBackReference
    private Profile profile;

    private Status status;
    private UUID connectedCrisisId;
}