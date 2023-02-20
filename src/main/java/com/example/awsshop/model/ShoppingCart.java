package com.example.awsshop.model;

import lombok.Data;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.*;

@Data
@Component
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCart {
    private String shippingAddress;
    private Set<SelectedProduct> selectedProducts;

    public ShoppingCart() {
        shippingAddress = "You don't have address right now";
        selectedProducts = new HashSet<>();
    }

    public double getTotalPrice() {
        var totalAmount = 0;
        for (var productSelected : selectedProducts) {
            totalAmount += Double.parseDouble(productSelected.getProduct().getPrice()) * productSelected.getQuantity();
        }
        return totalAmount;
    }

    public void clean() {
        setSelectedProducts(new HashSet<>());
    }
}
