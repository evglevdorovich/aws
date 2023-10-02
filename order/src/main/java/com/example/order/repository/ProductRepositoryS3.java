package com.example.order.repository;

import com.example.order.config.S3ConfigProperties;
import com.example.order.exception.S3NotFoundException;
import com.example.order.model.Product;
import com.example.order.utils.ProductUrlUtils;
import com.example.order.utils.S3BucketUrlCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetBucketLocationRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Repository
@Slf4j
public class ProductRepositoryS3 implements ProductRepository {

    private final S3Client amazonS3;
    private final ObjectMapper objectMapper;
    private final S3ConfigProperties s3ConfigProperties;
    private final S3BucketUrlCreator s3BucketUrlCreator;

    @Override
    public List<Product> getAll() {
        var bucketName = s3ConfigProperties.bucketName();
        var objectName = s3ConfigProperties.objectName();

        var getObjectRequest = GetObjectRequest
                .builder()
                .bucket(bucketName)
                .key(objectName)
                .build();
        var getBucketLocationRequest = GetBucketLocationRequest
                .builder()
                .bucket(bucketName)
                .build();

        var s3BucketLocation = amazonS3.getBucketLocation(getBucketLocationRequest).locationConstraintAsString();
        var bucketUrl = s3BucketUrlCreator.create(s3BucketLocation, bucketName);
        var s3Object = amazonS3.getObject(getObjectRequest);

        List<Product> products;
        try {
            products = objectMapper.readerForListOf(Product.class).readValue(s3Object);
            products = ProductUrlUtils.setAbsoluteUrlPhotoPath(bucketUrl, products);
        } catch (IOException e) {
            var errorMessage = String.format("bucket %s with object replaceNotifierName %s not found", bucketName, objectName);
            log.debug(errorMessage);
            throw new S3NotFoundException();
        }
        return products;
    }

}