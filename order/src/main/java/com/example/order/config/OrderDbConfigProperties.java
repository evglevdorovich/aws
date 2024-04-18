package com.example.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("db.order")
public record OrderDbConfigProperties(String tableName, String partitionKey, String warehouseStatusAttributeName,
                                      String paymentStatusAttributeName) {
}
