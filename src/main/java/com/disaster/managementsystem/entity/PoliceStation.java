package com.disaster.managementsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "police_stations")
@Where(clause = "is_deleted=0")
public class PoliceStation extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "district_id", referencedColumnName = "id")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private District district;

    @NotNull
    private String name;
    private String geoCode;
    private String latitude;
    private String longitude;
}
