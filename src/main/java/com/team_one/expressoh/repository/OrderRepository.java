package com.team_one.expressoh.repository;

import com.team_one.expressoh.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUsersId(Integer userId);
}