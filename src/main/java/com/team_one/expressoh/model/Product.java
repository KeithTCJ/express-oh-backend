package com.team_one.expressoh.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
    private List<Flavor> flavors;

    // Constructors

    public Product() {
    }

    public Product(Integer id, String sku, String name, String description, BigDecimal price, String imageURL, @NotNull(message = "Inventory count must be provided.") Integer inventoryCount, List<Flavor> flavors) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageURL = imageURL;
        this.inventoryCount = inventoryCount;
        this.flavors = flavors;
    }

    public void removeFlavor(Integer flavorId) {
        if (flavors != null) {
            flavors.removeIf(f -> f.getId().equals(flavorId));
        }
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(Integer inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public List<Flavor> getFlavors() {
        return flavors;
    }

    public void setFlavors(List<Flavor> flavors) {
        this.flavors = flavors;
    }
}