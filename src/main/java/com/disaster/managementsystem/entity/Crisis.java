package com.disaster.managementsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.disaster.managementsystem.domain.SelfAssignmentRequest;
import com.disaster.managementsystem.enums.Severity;
import com.disaster.managementsystem.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"selfAssignmentRequests", "profiles","policeStation", "district","division"})
@ToString(callSuper = true, exclude = {"selfAssignmentRequests", "profiles","policeStation", "district","division"})
@Entity
@Table(name = "crisis")
@Where(clause = "is_deleted=0")
@SuperBuilder(toBuilder = true)
public class Crisis extends BaseEntity {
    private String name;
    private String title;
    private String image;
    private String description;
    private boolean requiredHelp;
    @Convert(converter = Severity.SeverityConverter.class)
    private Severity severity;

    private String address;

    private LocalDateTime entryTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id", referencedColumnName = "id")
    @JsonBackReference
    private Division division;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", referencedColumnName = "id")
    @JsonBackReference
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "police_station_id", referencedColumnName = "id")
    @JsonBackReference
    private PoliceStation policeStation;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "crisis_id") // This specifies the foreign key in the Profile entity
    @JsonManagedReference
    private Set<Profile> profiles;

    @Convert(converter = Status.StatusConverter.class)
    private Status status;


    @ElementCollection(fetch = FetchType.EAGER, targetClass = SelfAssignmentRequest.class)
    @CollectionTable(name = "volunteer_request_on_crisis",
            joinColumns = @JoinColumn(name = "crisis_id", referencedColumnName = "id"))
    private Set<SelfAssignmentRequest> selfAssignmentRequests = new LinkedHashSet<>();
}