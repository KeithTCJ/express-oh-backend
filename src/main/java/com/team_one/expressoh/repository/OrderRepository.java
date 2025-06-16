package com.team_one.expressoh.repository;

import com.team_one.expressoh.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

        // JPQL query grouping by the month extracted from the orderDate field.
        @Query("SELECT FUNCTION('MONTH', o.orderDate) AS month, COUNT(o) AS orders " +
                "FROM Order o GROUP BY FUNCTION('MONTH', o.orderDate)")
        List<Object[]> findMonthlyOrderCounts();
    }
