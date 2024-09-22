package com.disaster.managementsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.disaster.managementsystem.enums.UserType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"roles", "profile"})
@ToString(callSuper = true, exclude = {"roles", "profile"})
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    @Column(name = "user_name", columnDefinition = "VARCHAR(255)", unique = true, nullable = false)
    private String userName;
    @Column(name = "password", columnDefinition = "VARCHAR(1024)", nullable = false)
    private String password;
    @Column(name = "email", columnDefinition = "VARCHAR(1024)", unique = true, nullable = false)
    private String email;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(name = "user_has_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @JsonIgnoreProperties("users")
    @JsonManagedReference
    @Builder.Default
    private Set<Role> roles = new LinkedHashSet<>();

    private boolean profileCreated;

    @Convert(converter = UserType.UserTypeConverter.class)
    private UserType userType = UserType.UNKNOWN;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    @JsonBackReference
    private Profile profile;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new LinkedHashSet<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }
}