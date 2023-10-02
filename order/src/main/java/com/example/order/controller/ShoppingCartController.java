package com.example.order.controller;

import com.example.order.model.Product;
import com.example.order.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping("/shoppingCarts")
    public String getShoppingCartProducts(Model model) {
        model.addAttribute("selectedProducts", shoppingCartService.getSelectedProducts());
        model.addAttribute("shippingAddress", shoppingCartService.getShippingAddress());
        model.addAttribute("totalAmount", shoppingCartService.getTotalPrice());
        return "shoppingCart";
    }

    @GetMapping("/shoppingCarts/shippingAddresses")
    @ResponseBody
    public String getShoppingCartShippingAddresses() {
        return shoppingCartService.getShippingAddress();
    }

    @ResponseBody
    @PostMapping("/shoppingCarts/products")
    public void createProductForShoppingCart(@RequestBody Product product) {
        shoppingCartService.addProduct(product);
    }

    @ResponseBody
    @PostMapping("/shoppingCarts/shippingAddresses")
    public void createShippingAddressForShoppingCart(@RequestBody String serviceAddress) {
        shoppingCartService.setShippingAddress(serviceAddress);
    }

}
