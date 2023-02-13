package com.example.awsshop.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCart {
    private String shippingAddress;
    private Map<Product, Long> productQuantityMap;

    public ShoppingCart() {
        shippingAddress = "address";
        productQuantityMap = new HashMap<>();
    }
}
