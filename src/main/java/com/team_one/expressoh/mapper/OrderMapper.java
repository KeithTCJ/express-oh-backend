package com.team_one.expressoh.mapper;

import com.team_one.expressoh.dto.OrderProductDTO;
import com.team_one.expressoh.dto.OrderResponseDTO;
import com.team_one.expressoh.model.Order;
import com.team_one.expressoh.model.OrderProduct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponseDTO toOrderResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalCost(order.getTotalCost());

        List<OrderProductDTO> productDTOs = order.getProducts().stream().map(this::toOrderProductDTO).collect(Collectors.toList());
        dto.setProducts(productDTOs);

        return dto;
    }

    private OrderProductDTO toOrderProductDTO(OrderProduct orderProduct) {
        OrderProductDTO dto = new OrderProductDTO();
        dto.setProductId(orderProduct.getProduct().getId());
        dto.setProductName(orderProduct.getProduct().getName());
        dto.setQuantity(orderProduct.getQuantity());
        return dto;
    }

    public List<OrderResponseDTO> toOrderResponseDTOs(List<Order> orders) {
        return orders.stream().map(this::toOrderResponseDTO).collect(Collectors.toList());
    }
}