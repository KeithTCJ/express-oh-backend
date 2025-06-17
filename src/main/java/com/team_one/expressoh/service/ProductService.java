package com.team_one.expressoh.service;

import com.team_one.expressoh.dto.ProductUpdateDTO;
import com.team_one.expressoh.model.Flavor;
import com.team_one.expressoh.model.Product;
import com.team_one.expressoh.repository.ProductRepository;
import com.team_one.expressoh.repository.FlavorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Value("${file.upload-dir}")
    private String uploadDir;

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

    @Transactional
    public Product createProduct(Product product, MultipartFile imageFile) throws IOException {
        // Check for duplicate SKU
        if (productRepository.existsBySku(product.getSku())) {
            throw new IllegalArgumentException("Product with SKU " + product.getSku() + " already exists");
        }

        // Ensure flavors are managed entities
        if (product.getFlavors() != null && !product.getFlavors().isEmpty()) {
            List<Integer> flavorIds = product.getFlavors().stream()
                    .map(Flavor::getId)
                    .toList();

            List<Flavor> managedFlavors = flavorRepository.findAllById(flavorIds);
            product.setFlavors(managedFlavors);
        }

        if (imageFile != null && !imageFile.isEmpty()){
            // Create a file structure to upload to uploads/images/filename.jpg
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            File img = new File(uploadDir + File.separator + fileName);
            imageFile.transferTo(img.toPath());
            product.setImageURL(String.format("/%s/%s", uploadDir, fileName));
        }

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Integer productId, ProductUpdateDTO productDto, MultipartFile imageFile) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Update basic fields
        product.setSku(productDto.getSku());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setInventoryCount(productDto.getInventoryCount());

        // Handle flavor updates
        if (productDto.getFlavors() != null) {
            if (productDto.getFlavors().isEmpty()) {
                product.getFlavors().clear();
            } else {
                List<Flavor> managedFlavors = flavorRepository.findAllById(productDto.getFlavors());
                product.setFlavors(managedFlavors);
            }
        }

        if (imageFile != null && !imageFile.isEmpty()){
            // Create a file structure to upload to uploads/images/filename.jpg
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            File img = new File(uploadDir + File.separator + fileName);
            imageFile.transferTo(img.toPath());
            product.setImageURL(String.format("/%s/%s", uploadDir, fileName));
        }

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

}