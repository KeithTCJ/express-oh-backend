package com.team_one.expressoh.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {

    private Integer orderId;
    private LocalDateTime orderDate;
    private Double totalCost;
    private List<OrderProductDTO> products; // Referencing standalone OrderProductDTO

    // Constructors, getters, and setters

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public List<OrderProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProductDTO> products) {
        this.products = products;
    }
}
