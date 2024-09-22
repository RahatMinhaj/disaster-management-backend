package com.disaster.managementsystem.entity;

import com.disaster.managementsystem.enums.*;
import jakarta.persistence.Entity;
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
@Table(name = "divisions")
@Where(clause = "is_deleted=0")
public class Division extends BaseEntity {
    private String geoCode;
    @NotNull
    private String name;
    private String latitude;
    private String longitude;
}
