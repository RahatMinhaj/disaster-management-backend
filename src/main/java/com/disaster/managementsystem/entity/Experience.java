package com.disaster.managementsystem.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@ToString
@Builder
public class Experience {
    private String companyName;
    private String department;
    private String position;
    private double totalExperience;
    private String workSummary;
}