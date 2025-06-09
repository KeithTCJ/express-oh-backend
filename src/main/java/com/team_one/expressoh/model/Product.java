package com.team_one.expressoh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data //Generates the getters, setters, toString, @RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="sku")
    @NotBlank(message = "Product SKU cannot be blank.")
    @Size(min = 3, message = "Product SKU must be at least 3 characters.")
    @Size(max = 255, message = "Supplier SKU must not be more than 255 characters.")
    private String sku;

    @Column(name = "name")
    @NotBlank(message = "Product Name cannot be blank.")
    @Size(min = 3, message = "Product Name must be at least 3 characters.")
    @Size(max = 255, message = "Product Name must not be more than 255 characters.")
    private String name;

    @Lob
    @Column(name = "description")
    @NotBlank(message = "Product Desc. cannot be blank.")
    @Size(min = 3, message = "Product Desc. must be at least 3 characters.")
    private String description;

    @Column(name = "price")
    @DecimalMin(value = "0.0", inclusive = false, message = "Product Price must be more than 0.")
    @Digits(integer = 10, fraction = 2, message = "Product Price must be numerical.")
    private BigDecimal price;

    @Column(name = "imageurl")
    @NotBlank(message = "Image URL cannot be blank.")
    @Size(min = 3, message = "IMAGE URL must be at least 3 characters.")
    @Pattern(
            regexp = "^(https?)://.*$",
            message = "Invalid URL format. Must start with http or https."
    )
    private String imageURL;

    @Column(name = "inventory", nullable = false)
    @NotNull(message = "Inventory count must be provided.")
    @Min(value = 0, message = "Inventory count cannot be negative.")
    private Integer inventoryCount;


    // automatically create product_flavor table
    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinTable(
            name = "product_flavor",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "flavor_id")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Flavor> flavors;

}