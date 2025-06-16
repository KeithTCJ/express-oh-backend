package com.team_one.expressoh.service;

import com.team_one.expressoh.dto.ProductUpdateDTO;
import com.team_one.expressoh.model.Flavor;
import com.team_one.expressoh.model.Product;
import com.team_one.expressoh.repository.ProductRepository;
import com.team_one.expressoh.repository.FlavorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final FlavorRepository flavorRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, FlavorRepository flavorRepository) {
        this.productRepository = productRepository;
        this.flavorRepository = flavorRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        if (productRepository.existsBySku(product.getSku())) {
            throw new IllegalArgumentException("Product with SKU " + product.getSku() + " already exists");
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Integer productId, ProductUpdateDTO productDto) {
        // Find the product by ID or throw an exception.
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Update basic fields.
        product.setSku(productDto.getSku());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setInventoryCount(productDto.getInventory());
        product.setImageURL(productDto.getImageurl());

        // Process flavor association.
        if (productDto.getFlavors() != null && !productDto.getFlavors().isEmpty()) {
            List<Flavor> flavors = flavorRepository.findAllById(productDto.getFlavors());
            product.setFlavors(flavors);
        } else {
            // Optionally, clear the flavors if an empty list is provided.
            product.getFlavors().clear();
        }

        // Save and return the updated product.
        return productRepository.save(product);
    }

    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}