package com.disaster.managementsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.disaster.managementsystem.enums.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString(callSuper = true)
@Entity
@Table(name = "inventories")
@Where(clause = "is_deleted=0")
public class Inventory extends BaseEntity {
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id", referencedColumnName = "id")
    @JsonBackReference
    private ProductCategory productCategory;

    @Convert(converter = StockType.StockTypeConverter.class)
    private StockType stockType = StockType.UNKNOWN;

    private LocalDate time;

    @Convert(converter = QuantityType.QuantityTypeConverter.class)
    private QuantityType quantityType = QuantityType.UNKNOWN;

    private double quantity;

    private BigDecimal moneyExpense;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    @JsonBackReference
    private Profile profile;


}
