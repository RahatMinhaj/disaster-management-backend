package com.disaster.managementsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"users"})
@SuperBuilder(toBuilder = true)
public class Role extends BaseEntity{
    @Serial
    private static final long serialVersionUID = -3089987029403456184L;
//    nvarchar not supported
//    @Column(nullable = false, columnDefinition = "NVARCHAR2(250)", unique = true)
    private String name;
//    @Column(nullable = false, columnDefinition = "NVARCHAR2(250)")
    private String display;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles", cascade = CascadeType.REFRESH)
    @JsonBackReference
    private Set<User> users;
}