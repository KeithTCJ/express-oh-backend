package com.team_one.expressoh.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team_one.expressoh.dto.ProductUpdateDTO;
import com.team_one.expressoh.model.Product;
import com.team_one.expressoh.service.ProductService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<Product> createProduct(
            @Valid @RequestPart("product") String data,
            @Nullable @RequestPart("image") MultipartFile imageFile) throws Exception {

        // textual data sent as product will need to be mapped to Product class first
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = objectMapper.readValue(data, Product.class);

        try {
            Product createdProduct = productService.createProduct(product, imageFile);
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
                                                 @Valid @RequestPart("product") String data,
                                                 @Nullable @RequestPart("image") MultipartFile imageFile) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        ProductUpdateDTO product = objectMapper.readValue(data, ProductUpdateDTO.class);

        try {
            Product updatedProduct = productService.updateProduct(id, product, imageFile);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // -------------------------------
    // NEW: Upload product image
    // -------------------------------
    @PostMapping("/{productId}/image")
    public ResponseEntity<?> uploadProductImage(
            @PathVariable Integer productId,
            @RequestParam("image") MultipartFile file
    ) {
        try {
            Optional<Product> optionalProduct = productService.getProductById(productId);
            if (optionalProduct.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            String fileName = file.getOriginalFilename();
            String uploadDir = "uploads/images/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Save image URL to DB
            Product product = optionalProduct.get();
            String imageUrl = "/images/" + fileName;
            product.setImageURL(imageUrl);
            productService.save(product);

            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Image upload failed: " + e.getMessage());
        }
    }
}
