package com.example.awsshop.repository;

import com.example.awsshop.exception.FileNotReadException;
import com.example.awsshop.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;



@RequiredArgsConstructor
@Repository
@Slf4j
public class ProductRepositoryFileIO implements ProductRepository {

    private final ObjectMapper objectMapper;
    @Value("${products.file.path}")
    private String productFilePath;

    @Override
    public List<Product> getAll() {
        List<Product> products;
        try(var resource = this.getClass().getResourceAsStream(productFilePath)){
            products = objectMapper.readerForListOf(Product.class).readValue(resource);
        } catch (IOException e) {
            throw new FileNotReadException(String.format("error while reading file from %s", productFilePath), e);
        }
        return products;
    }
}
