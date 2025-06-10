package com.team_one.expressoh.repository;

import com.team_one.expressoh.model.OrderProduct;
import com.team_one.expressoh.model.Order;
import com.team_one.expressoh.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {
    List<OrderProduct> findByOrder(Order order);
    List<OrderProduct> findByProduct(Product product);
}