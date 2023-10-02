package com.example.order.utils;

import com.example.order.model.SelectedProduct;

import java.math.BigDecimal;

public class SelectedProductPriceCalculator {

    private SelectedProductPriceCalculator() {
    }

    public static BigDecimal getTotalPrice(SelectedProduct product) {
        return product.getPrice().multiply(new BigDecimal(product.getQuantity()));
    }
}
