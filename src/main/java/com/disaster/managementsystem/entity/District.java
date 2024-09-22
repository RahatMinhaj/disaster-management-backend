package com.disaster.managementsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString(callSuper = true)
@Entity
@Table(name = "districts")
@Where(clause = "is_deleted=0")
public class District extends BaseEntity {
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id", referencedColumnName = "id")
    @JsonBackReference
    private Division division;
    private String name;
    private String geoCode;
    private String latitude;
    private String longitude;
}
