package com.team_one.expressoh.service;

import com.team_one.expressoh.model.Order;
import com.team_one.expressoh.model.Users;
import com.team_one.expressoh.repository.OrderRepository;
import com.team_one.expressoh.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UsersRepository usersRepository;

    public Map<String, Integer> getMonthlyOrders() {
        List<Object[]> results = orderRepository.findMonthlyOrderCounts();
        Map<String, Integer> monthlyOrders = new HashMap<>();

        for (Object[] row : results) {
            Integer monthNumber = (Integer) row[0]; // numeric month (1 to 12)
            Number countNumber = (Number) row[1];
            String monthName = getMonthName(monthNumber);
            monthlyOrders.put(monthName, countNumber.intValue());
        }

        return monthlyOrders;
    }

    private String getMonthName(int month) {
        String[] monthNames = { "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December" };
        return monthNames[month - 1];
    }

    public List<Order> getOrdersByUserEmail(String email) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUsers(user);
    }
}
