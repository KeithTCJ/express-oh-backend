package com.team_one.expressoh.controllers;

import com.team_one.expressoh.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "*")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/monthly-orders")
    public ResponseEntity<Map<String, Integer>> getMonthlyOrders() {
        Map<String, Integer> monthlyOrders = orderService.getMonthlyOrders();
        return ResponseEntity.ok(monthlyOrders);
    }
}
