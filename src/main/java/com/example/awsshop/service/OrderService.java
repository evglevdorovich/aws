package com.example.awsshop.service;

import com.example.awsshop.model.Order;
import com.example.awsshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order save(Order order) {
        order.addOrderToSelectedProduct();
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> findOrderByUserId(Long userId) {
        return orderRepository.findOrderByUserId(userId);
    }
}
