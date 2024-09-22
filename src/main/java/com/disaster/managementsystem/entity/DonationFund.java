package com.disaster.managementsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString(callSuper = true)
@Entity
@Table(name = "donation_funds")
@Where(clause = "is_deleted=0")
public class DonationFund extends BaseEntity {
    private String name;
    private String phone;
    private double donationAmount;
    private String transactionId;
}
