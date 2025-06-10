package com.team_one.expressoh.controller;

import com.team_one.expressoh.exception.ResourceNotFoundException;
import com.team_one.expressoh.model.Product;
import com.team_one.expressoh.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/api/product")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // GET all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

    // GET product by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found with ID: " + id);
        }
    }

    // POST new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    // PUT update product
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody Product updatedProduct) {
        Optional<Product> existingProductOpt = productRepository.findById(id);
        if (existingProductOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found with ID: " + id);
        }

        Product existingProduct = existingProductOpt.get();
        existingProduct.setSku(updatedProduct.getSku());
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setImageURL(updatedProduct.getImageURL());
        existingProduct.setInventoryCount(updatedProduct.getInventoryCount());
        existingProduct.setFlavors(updatedProduct.getFlavors());

        productRepository.save(existingProduct);
        return ResponseEntity.ok(existingProduct);
    }

    // DELETE product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found with ID: " + id);
        }

        productRepository.deleteById(id);
        return ResponseEntity.ok("Product deleted successfully.");
    }

    @GetMapping("/flavors")
    public ResponseEntity<List<Product>> getProductsByFlavors(@RequestParam List<String> names) {
        List<Product> products = productRepository.findByFlavorNames(names);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found for selected flavors");
        }
        return ResponseEntity.ok(products);
    }
}

