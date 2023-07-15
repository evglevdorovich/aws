package com.example.order.controller;

import com.example.order.service.OrderService;
import com.example.order.service.PaymentService;
import com.example.order.service.WarehouseService;
import com.example.order.utils.PaymentObjectDtoExtractor;
import com.example.order.utils.WarehouseObjectDtoExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final WarehouseService warehouseService;
    private final PaymentObjectDtoExtractor paymentObjectDtoExtractor;
    private final WarehouseObjectDtoExtractor warehouseObjectDtoExtractor;

    @GetMapping("/checkout")
    public String orderProducts() {
        var savedOrder = orderService.saveFromShoppingCart();
        var payment = paymentObjectDtoExtractor.extractFromOrderForWithdrawal(savedOrder);
        var warehouseDto = warehouseObjectDtoExtractor.extractFromOrderForWithdrawal(savedOrder);
        warehouseService.sendProducts(warehouseDto);
        paymentService.sendPayment(payment);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String getOrders(Model model) {
        var orders = orderService.findOrdersByCurrentUser();
        model.addAttribute("orders", orders);
        return "orders";
    }

}
