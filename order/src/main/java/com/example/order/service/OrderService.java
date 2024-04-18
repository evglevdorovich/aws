package com.example.order.service;

import com.example.order.model.ApplicationUser;
import com.example.order.model.Order;
import com.example.order.model.OrderStatus;
import com.example.order.model.OrderedProduct;
import com.example.order.repository.OrderRepository;
import com.example.order.utils.ObjectMapperCollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartService shoppingCartService;
    private final ObjectMapperCollectionUtils objectMapperCollectionUtils;
    private final ApplicationUserService userService;
    private final SnsClient snsClient;
    @Value("${mail.topic.arn}")
    private String topicArn;

    public Order saveFromShoppingCart() {
        var selectedProducts = shoppingCartService.getSelectedProducts();
        var orderedProducts = objectMapperCollectionUtils.mapAllToList(selectedProducts, OrderedProduct.class);
        var currentUserId = userService.getCurrentUserId().toString();

        var order = Order.builder()
                .warehouseStatus(OrderStatus.IN_PROGRESS)
                .paymentStatus(OrderStatus.IN_PROGRESS)
                .shippingAddress(shoppingCartService.getShippingAddress())
                .createdDate(Instant.now())
                .totalAmount(shoppingCartService.getTotalPrice())
                .products(orderedProducts)
                .userId(currentUserId)
                .id(UUID.randomUUID().toString())
                .build();


        return orderRepository.upsert(order);
    }

    public Optional<Order> findById(String orderId) {
        return orderRepository.findById(orderId);
    }

    public void updateWarehouseStatus(String orderId, OrderStatus orderStatus){
        orderRepository.updateWarehouseStatus(orderId, orderStatus);
    }

    public void updatePaymentStatus(String orderId, OrderStatus orderStatus){
        orderRepository.updatePaymentStatus(orderId, orderStatus);
    }

    public List<Order> findOrdersByCurrentUser() {
        var principal = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userDetails = userService.loadUserByUsername(principal.getUsername());
        return orderRepository.findOrderByUserId(userDetails.getId().toString());
    }

    public void sendFailoverSnsMessage(String message) {

        var publishRequestMail = PublishRequest.builder()
                .message(message)
                .topicArn(topicArn)
                .build();

        snsClient.publish(publishRequestMail);
    }


}
