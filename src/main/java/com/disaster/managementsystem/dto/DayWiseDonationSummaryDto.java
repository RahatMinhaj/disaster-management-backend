package com.disaster.managementsystem.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class DayWiseDonationSummaryDto {
    private LocalDate donationDay;
    private double totalAmount;
}
