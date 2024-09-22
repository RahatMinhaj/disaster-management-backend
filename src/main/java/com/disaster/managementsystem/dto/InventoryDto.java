package com.disaster.managementsystem.dto;

import com.disaster.managementsystem.dto.core.BaseDto;
import com.disaster.managementsystem.enums.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class InventoryDto extends BaseDto {
    private LocalDate time;
    private QuantityType quantityType;
    private double quantity;
    private BigDecimal moneyExpense;
    private StockType stockType;
    private ProductCategoryDto productCategoryDto;
    private ProfileDto profileDto;
}
