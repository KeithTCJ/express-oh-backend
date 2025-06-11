package com.team_one.expressoh.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "email", columnNames = "email")
})
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Core authentication fields
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Email must be valid.")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password cannot be blank.")
    private String password;

    @NotNull(message = "Role must be set.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumRole role;

    // One-to-one relation with Profile
    // This change guarantees that every time you load a Users object
    // (for instance, in the sign‑in process),
    // the associated Profile is retrieved immediately,
    // so the extra fields (firstName, lastName, phone, etc.) are available.

    @OneToOne(
            mappedBy = "users",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @PrimaryKeyJoinColumn
    private Profile profile;

    public Users() {
        // Default constructor required by JPA
    }

    public Users(String email, String password, EnumRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }


    // Getters and setters

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EnumRole getRole() {
        return role;
    }

    public void setRole(EnumRole role) {
        this.role = role;
    }

    // Users Class should incorporate UserDetails Service
    // where the following overridden methods apply
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Profile getProfile() {
        return profile;
    }

    // Convenience method for bidirectional linking for users to update profile
    public void setProfile(Profile profile) {
        this.profile = profile;
        if (profile != null) {
            profile.setUsers(this);
            profile.setUsersId(this.id); //Link the ID here if you want to set it programmatically
        }
    }
}