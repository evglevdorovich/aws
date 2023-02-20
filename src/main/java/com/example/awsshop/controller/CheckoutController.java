package com.example.awsshop.controller;

import com.example.awsshop.exception.PaymentFailedException;
import com.example.awsshop.model.ApplicationUser;
import com.example.awsshop.model.Order;
import com.example.awsshop.model.OrderStatus;
import com.example.awsshop.model.ShoppingCart;
import com.example.awsshop.service.ApplicationUserService;
import com.example.awsshop.service.OrderService;
import com.example.awsshop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final ShoppingCart shoppingCart;
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final ApplicationUserService userService;


    @GetMapping("/checkout")
    public String createShippingAddressForShoppingCart() {
        try {
            paymentService.buyProducts(shoppingCart);
        } catch (PaymentFailedException e) {
            var principal = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var userDetails = userService.loadUserByUsername(principal.getUsername());
            var order = Order.builder()
                    .orderStatus(OrderStatus.COMPLETE)
                    .totalAmount(BigDecimal.valueOf(shoppingCart.getTotalPrice()))
                    .userId(userDetails.getId())
                    .selectedProductList(shoppingCart.getSelectedProducts())
                    .build();
            orderService.save(order);
        }
        shoppingCart.clean();
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String getOrders(Model model) {
        var principal = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userDetails = userService.loadUserByUsername(principal.getUsername());
        var orders = orderService.findOrderByUserId(userDetails.getId());
        model.addAttribute("orders", orders);
        return "orders";
    }


}
