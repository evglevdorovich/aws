package com.example.order.repository;

import com.example.order.model.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> getAll();
}
