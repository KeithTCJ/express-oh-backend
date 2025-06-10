package com.team_one.expressoh.repository;

import com.team_one.expressoh.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Basic CRUD methods provided by JpaRepository
    // Find all products by flavor(tag) name
    @Query("SELECT DISTINCT p FROM Product p JOIN p.flavors f WHERE f.name IN :flavorNames")
    List<Product> findByFlavorNames(@Param("flavorNames") List<String> flavorNames);
}