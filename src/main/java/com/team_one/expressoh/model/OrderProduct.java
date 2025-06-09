package com.team_one.expressoh.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.Objects;


@Entity
public class OrderProduct {

    //@ManyToOne
    @ManyToOne(fetch = FetchType.LAZY, optional = false)            // Foreign key reference to Orders entity
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // resolve BeanSerialization issue
    @OnDelete(action = OnDeleteAction.RESTRICT)                     // manages the foreign constraint of parent entity (users)
    @Id
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)            // Foreign key reference to Orders entity
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // resolve BeanSerialization issue
    @OnDelete(action = OnDeleteAction.RESTRICT)                     // manages the foreign constraint of parent entity (users)
    @Id
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    // Constructors

    public OrderProduct() {
    }

    public OrderProduct(Order order, Product product, Integer quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}