package com.example.awsshop.service;

import com.example.awsshop.exception.PaymentFailedException;
import com.example.awsshop.model.ApplicationUser;
import com.example.awsshop.model.Order;
import com.example.awsshop.model.OrderStatus;
import com.example.awsshop.model.ShoppingCart;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final WarehouseService warehouseService;
    private final UserVirtualPaymentAccountService userVirtualPaymentAccountService;
    private final ApplicationUserService userService;
    private final OrderService orderService;

    @Transactional
    public void buyProducts(ShoppingCart shoppingCart) {
        var principal = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userDetails = userService.loadUserByUsername(principal.getUsername());
        warehouseService.reserveProductsFromWarehouse(shoppingCart);
        userVirtualPaymentAccountService.decreaseMoneyById(shoppingCart.getTotalPrice(), userDetails.getId());

        var order = Order.builder()
                .orderStatus(OrderStatus.COMPLETE)
                .totalAmount(BigDecimal.valueOf(shoppingCart.getTotalPrice()))
                .userId(userDetails.getId())
                .selectedProductList(shoppingCart.getSelectedProducts())
                .shippingAddress(shoppingCart.getShippingAddress())
                .build();
        orderService.save(order);
    }

}
