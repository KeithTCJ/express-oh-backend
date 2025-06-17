package com.team_one.expressoh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;


@Entity
public class Flavor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // primary key, auto-incremented
    private Integer id;

    @Column(name = "name")
    @NotBlank(message = "Flavor name cannot be blank.")
    @Size(min = 3, message = "Flavor name must be at least 3 characters.")
    @Size(max = 255, message = "Flavor name must not be more than 255 characters.")
    private String name;

    // automatically create product_flavor table
    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.DETACH,
                    CascadeType.REFRESH
            }
    )

    @JoinTable(
            name = "product_flavor",
            joinColumns = @JoinColumn(name = "flavor_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )

    @JsonIgnore
    private List<Product> products;

    public Flavor() {
    }
    public Flavor(Integer id) {
        this.id = id;
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
