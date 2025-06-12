package com.team_one.expressoh.repository;

import com.team_one.expressoh.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsBySku(String sku);
    List<Product> findProductsByFlavorsId(Integer flavorId);
}