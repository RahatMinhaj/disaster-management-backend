package com.disaster.managementsystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.disaster.managementsystem.enums.GenderType;
import com.disaster.managementsystem.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString(callSuper = true)
@Entity
@Table(name = "profiles")
@SQLDelete(sql = "UPDATE profiles SET is_deleted=1 WHERE id=?")
@Where(clause = "is_deleted=0")
public class Profile extends BaseEntity {

    @Column(name = "name", columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "mobileNo")
    private String mobileNo;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "gender_type")
    private GenderType genderType;

    private LocalDate dateOfBirth;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    private User user;

    @Convert(converter = Status.StatusConverter.class)
    private Status status;
}