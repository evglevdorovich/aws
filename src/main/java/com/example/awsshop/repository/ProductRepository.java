package com.example.awsshop.repository;

import com.example.awsshop.model.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> getAll();
}
