package com.example.awsshop.repository;

import com.example.awsshop.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(value = "Order.selectedProductList")
    List<Order> findOrderByUserId(Long userId);
}
