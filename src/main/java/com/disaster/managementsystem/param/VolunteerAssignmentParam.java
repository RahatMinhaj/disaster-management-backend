package com.disaster.managementsystem.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerAssignmentParam {
    private Set<UUID> volunteerIds;
}
