package com.example.order.repository;

import com.example.order.model.Order;
import com.example.order.model.OrderStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository {

    Order upsert(Order order);
    List<Order> findOrderByUserId(String userId);

    Optional<Order> findById(String orderId);

    void updateWarehouseStatus(String orderId, OrderStatus orderStatus);

    void updatePaymentStatus(String orderId, OrderStatus orderStatus);

}
