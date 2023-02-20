package com.example.awsshop.repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3control.model.NotFoundException;
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
public class ProductRepositoryS3 implements ProductRepository {

    private final AmazonS3 amazonS3;
    private final ObjectMapper objectMapper;
    @Value("${product.bucketName}")
    private String productBucketName;
    @Value("${product.objectName}")
    private String objectName;

    @Override
    public List<Product> getAll() {
        var s3Object = amazonS3.getObject(productBucketName, objectName).getObjectContent();
        List<Product> products;
        try {
            products = objectMapper.readerForListOf(Product.class).readValue(s3Object);
            products.forEach(product -> product.setPhotoUrl(amazonS3.getUrl(productBucketName, product.getPhotoUrl()).toString()));
        } catch (IOException e) {
            var errorMessage = String.format("bucket %s with object name %s not found", productBucketName, objectName);
            log.debug(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return products;
    }
}