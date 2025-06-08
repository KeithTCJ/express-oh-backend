package com.team_one.expressoh.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="user", uniqueConstraints = {@UniqueConstraint(name = "email", columnNames = "email")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // primary key, auto-incremented
    Integer id;

    @Column(nullable = false, unique = true)                    // prevents null values and email duplicates
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",  // regular expression for email
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Email is invalid")
    String email;

    @Column(nullable = false)
    @NotBlank(message="Password cannot be blank")
    // @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password is not strong.")
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role cannot be blank")
    private EnumRole role;

    public User() {                 // empty constructor
        role = null;
    }

    public User(String email, String password, EnumRole role) {    // parameterized constructor, id not needed as it is auto generated
        this.email = email;
        this.role = null;
    }

    // getters

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public EnumRole getRole() {
        return role;
    }

    // setters

    // setter for id omitted since again, it is auto-generated

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(EnumRole role) {
        this.role = role;
    }
}
