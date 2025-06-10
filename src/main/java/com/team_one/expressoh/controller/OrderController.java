package com.team_one.expressoh.controller;

import com.team_one.expressoh.exception.ResourceNotFoundException;
import com.team_one.expressoh.model.Order;
import com.team_one.expressoh.model.Users;
import com.team_one.expressoh.repository.OrderRepository;
import com.team_one.expressoh.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/api/order")
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("")
    public ResponseEntity<Object> getAllOrders() {
        List<Order> result = orderRepository.findAll();
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("No orders found.");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable("id") Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Object> createOrder(@RequestParam Integer userId,
                                              @RequestParam Double totalCost) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Order order = new Order(user, LocalDateTime.now(), totalCost);
        Order savedOrder = orderRepository.save(order);

        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable("id") Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        orderRepository.delete(order);
        return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
    }
}