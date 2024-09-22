package com.disaster.managementsystem.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonationFundParam extends BaseParam {
    @Schema(hidden = true)
    private UUID id;

    private String name;
    private String phone;
    private double donationAmount;
    private String transactionId;
}
