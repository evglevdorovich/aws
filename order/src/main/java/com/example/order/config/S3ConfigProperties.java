package com.example.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("s3")
public record S3ConfigProperties(String bucketName, String objectName) {
}
