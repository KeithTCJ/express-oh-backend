package com.team_one.expressoh.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {

    // id integer
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // primary key, auto-incremented
    Integer id;

    // user_id integer foreign key
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key reference to User entity
    private User user;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate; // Uses LocalDateTime instead of Integer

    @Column(name = "total_cost", nullable = false)
    private Double totalCost;

    // Constructors, getters, and setters
    public Order() {}

    public Order(User user, LocalDateTime orderDate, Double totalCost) {
        this.user = user;
        this.orderDate = orderDate;
        this.totalCost = totalCost;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    @OneToMany(mappedBy = "order")
    private Set<OrderProduct> products;

}
