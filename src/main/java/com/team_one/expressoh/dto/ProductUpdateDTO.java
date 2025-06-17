package com.team_one.expressoh.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductUpdateDTO {
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer inventoryCount;
    private String imageurl;
    private List<Integer> flavors; // List of flavor IDs

    // Getters and setters
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
    public Integer getInventoryCount() {
        return inventoryCount;
    }
    public void setInventoryCount(Integer inventoryCount) {
        this.inventoryCount = inventoryCount;
    }
    public String getImageurl() {
        return imageurl;
    }
    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
    public List<Integer> getFlavors() {
        return flavors;
    }
    public void setFlavors(List<Integer> flavors) {
        this.flavors = flavors;
    }
}
