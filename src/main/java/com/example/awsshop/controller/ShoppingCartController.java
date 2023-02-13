package com.example.awsshop.controller;

import com.example.awsshop.model.Product;
import com.example.awsshop.model.ShoppingCart;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCart shoppingCart;

    @GetMapping("/shoppingCarts/products")
    public Map<Product, Long> getShoppingCartProducts() {
        return shoppingCart.getProductQuantityMap();
    }

    @GetMapping("/shoppingCarts/shippingAddresses")
    public String getShoppingCartShippingAddresses() {
        return shoppingCart.getShippingAddress();
    }

    @PostMapping("/shoppingCarts/products")
    public void createProductForShoppingCart(@RequestBody Product product) {
        var productQuantityMap = shoppingCart.getProductQuantityMap();
        var quantity = productQuantityMap.getOrDefault(product, 0L);
        productQuantityMap.put(product, ++quantity);
    }

    @PostMapping("/shoppingCarts/shippingAddresses")
    public void createShippingAddressForShoppingCart(String serviceAddress) {
        shoppingCart.setShippingAddress(serviceAddress);
    }

}
