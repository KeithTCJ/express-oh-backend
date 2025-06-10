package com.team_one.expressoh.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "email", columnNames = "email")
})
public class Users {

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
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", referencedColumnName = "id", unique = true, nullable = false)
    @JsonManagedReference
    private Profile profile;

    public Users() {
        // Default constructor required by JPA
    }

    public Users(String email, String password, EnumRole role, Profile profile) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.profile = profile;
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

    public String getPassword() {
        return password;
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}