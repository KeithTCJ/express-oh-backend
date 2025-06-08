package com.team_one.expressoh.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "flavor")
public class Flavor {

    @Column (name = "id")
    @NotBlank
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // primary key, auto-incremented
    Integer id;

    @Column(name = "name")
    @NotBlank(message = "Flavor name cannot be blank.")
    @Size(min = 3, message = "Flavor name must be at least 3 characters.")
    @Size(max = 255, message = "Flavor name must not be more than 255 characters.")
    private String name;

    public Flavor() {
    }

    public Flavor(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
