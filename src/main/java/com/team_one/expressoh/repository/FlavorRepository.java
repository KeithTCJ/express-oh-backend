package com.team_one.expressoh.repository;

import com.team_one.expressoh.model.Flavor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlavorRepository extends JpaRepository<Flavor, Integer> {

    // Optional filter for GET /flavor?name=...
    List<Flavor> findByNameContaining(String name);

    // Get all flavors for a given product ID
    List<Flavor> findFlavorsByProductsId(Integer productId);
}