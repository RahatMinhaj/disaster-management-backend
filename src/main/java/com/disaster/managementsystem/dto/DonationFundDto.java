package com.disaster.managementsystem.dto;

import com.disaster.managementsystem.dto.core.BaseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class DonationFundDto extends BaseDto {
    private String name;
    private String phone;
    private double donationAmount;
    private String transactionId;
}
