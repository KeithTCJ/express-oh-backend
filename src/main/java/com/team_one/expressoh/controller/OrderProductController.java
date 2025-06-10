package com.team_one.expressoh.controller;

import com.team_one.expressoh.exception.ResourceNotFoundException;
import com.team_one.expressoh.model.Order;
import com.team_one.expressoh.model.OrderProduct;
import com.team_one.expressoh.model.Product;
import com.team_one.expressoh.repository.OrderProductRepository;
import com.team_one.expressoh.repository.OrderRepository;
import com.team_one.expressoh.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api/orderproduct")
@CrossOrigin("*")
public class OrderProductController {

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("")
    public ResponseEntity<Object> createOrderProduct(@RequestParam Integer orderId,
                                                     @RequestParam Integer productId,
                                                     @RequestParam Integer quantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        OrderProduct op = new OrderProduct(order, product, quantity);
        OrderProduct saved = orderProductRepository.save(op);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Object> getProductsByOrder(@PathVariable Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        List<OrderProduct> result = orderProductRepository.findByOrder(order);
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("No products found for order.");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Object> getOrdersByProduct(@PathVariable Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        List<OrderProduct> result = orderProductRepository.findByProduct(product);
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("No orders found containing the product.");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}