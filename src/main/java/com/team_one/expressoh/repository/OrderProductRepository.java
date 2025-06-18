package com.team_one.expressoh.repository;

import com.team_one.expressoh.model.OrderProduct;
import com.team_one.expressoh.model.OrderProductId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductId> {}
