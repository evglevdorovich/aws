package com.example.order.service;

import com.example.order.model.Product;
import com.example.order.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private List<Product> products;

    @PostConstruct
    public void replaceProducts(){
        products = productRepository.getAll();
    }

    public List<Product> getAll() {
        return products;
    }

}
