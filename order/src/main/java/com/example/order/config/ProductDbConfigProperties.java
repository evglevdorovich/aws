package com.example.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("db.product")
public record ProductDbConfigProperties(String tableName, String partitionKey, String sortKey) {
}