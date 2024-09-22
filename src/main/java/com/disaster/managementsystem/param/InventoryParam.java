package com.disaster.managementsystem.param;

import com.disaster.managementsystem.enums.QuantityType;
import com.disaster.managementsystem.enums.StockType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryParam extends BaseParam {
    @Schema(hidden = true)
    private UUID id;

    private UUID productCategoryId;
    private StockType stockType;
    private LocalDate time;
    private QuantityType quantityType;
    private double quantity;
    private BigDecimal moneyExpense;
    private UUID profileId;

}
