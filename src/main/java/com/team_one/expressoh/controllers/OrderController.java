package com.team_one.expressoh.controllers;

import com.team_one.expressoh.dto.DtoOrderProduct;
import com.team_one.expressoh.dto.DtoOrderRequest;
import com.team_one.expressoh.model.*;
import com.team_one.expressoh.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UsersRepository usersRepository;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody DtoOrderRequest orderRequest,
                                         Authentication authentication) throws Exception {

        // 1. Get logged-in user by username
        String username = authentication.getName();
        Users user = usersRepository.findByEmail(username)
                .orElseThrow(() -> new Exception("User not found"));

        // 2. Create new Order instance
        Order order = new Order();
        order.setUsers(user);
        order.setOrderDate(LocalDateTime.now());

        // 3. Initialize total cost accumulator
        double totalCost = 0.0;

        // 4. Loop through all products and save OrderProduct entries
        for (DtoOrderProduct dtoProduct : orderRequest.getProducts()) {
            Product product = productRepository.findById(dtoProduct.getProductId())
                    .orElseThrow(() -> new Exception("Product with ID " + dtoProduct.getProductId() + " not found"));

            int quantity = dtoProduct.getQuantity();

            // Calculate product total and add to total cost
            BigDecimal productTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            totalCost += productTotal.doubleValue();
        }

        // 5. Set totalCost and save order now
        order.setTotalCost(totalCost);
        order = orderRepository.save(order);

        // 6. Now save the OrderProduct entries
        for (DtoOrderProduct dtoProduct : orderRequest.getProducts()) {
            Product product = productRepository.findById(dtoProduct.getProductId())
                    .orElseThrow(() -> new Exception("Product with ID " + dtoProduct.getProductId() + " not found"));

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(dtoProduct.getQuantity());
            orderProductRepository.save(orderProduct);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully.");
    }


}
