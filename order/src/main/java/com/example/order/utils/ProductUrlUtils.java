package com.example.order.utils;

import com.example.order.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductUrlUtils {
    private ProductUrlUtils() {
    }

    public static List<Product> setAbsoluteUrlPhotoPath(String bucketUrl, List<Product> products) {
        products.forEach(product -> product.setPhotoUrl(bucketUrl + product.getPhotoUrl()));
        return products;
    }
}
