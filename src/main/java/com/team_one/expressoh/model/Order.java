package com.team_one.expressoh.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {

    // id integer
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // primary key, auto-incremented
    Integer id;

    // users_id integer foreign key
    @ManyToOne(fetch = FetchType.LAZY, optional = false)            // Foreign key reference to Users entity
    @JoinColumn(name = "users_id", nullable = false)                // Many Orders come from a users
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // resolve BeanSerialization issue
    @OnDelete(action = OnDeleteAction.RESTRICT)                     // manages the foreign constraint of parent entity (users)
    private Users users;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate; // Uses LocalDateTime instead of Integer

    @Column(name = "total_cost", nullable = false)
    private Double totalCost;

    @OneToMany(mappedBy = "order")
    private Set<OrderProduct> products;

    // Constructors, getters, and setters
    public Order() {}

    public Order(Users users, LocalDateTime orderDate, Double totalCost) {
        this.users = users;
        this.orderDate = orderDate;
        this.totalCost = totalCost;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public Users getUsers() {
        return users;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setUsers(Users users) {
        this.users = users;
    }


    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

}
