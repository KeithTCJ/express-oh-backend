package com.team_one.expressoh.controllers;

import com.team_one.expressoh.dto.ProductUpdateDTO;
import com.team_one.expressoh.model.Product;
import com.team_one.expressoh.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin(origins = "*")
public class AdminProductController {

    private final ProductService productService;

    @Autowired
    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.ok(createdProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to update the product with its basic info and flavor associations.
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id,
                                                 @RequestBody ProductUpdateDTO productDto) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDto);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}