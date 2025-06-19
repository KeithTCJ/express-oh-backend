package com.team_one.expressoh.repository;

import com.team_one.expressoh.model.Order;
import com.team_one.expressoh.model.Users;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT FUNCTION('MONTH', o.orderDate) AS month, COUNT(o) AS orders " +
            "FROM Order o GROUP BY FUNCTION('MONTH', o.orderDate)")
    List<Object[]> findMonthlyOrderCounts();

    // Add this method to fetch orders by user
    List<Order> findByUsers(Users user);

    @Query("SELECT o FROM Order o WHERE o.users.email = :email")
    List<Order> findOrdersByUserEmail(@Param("email") String email);
}
