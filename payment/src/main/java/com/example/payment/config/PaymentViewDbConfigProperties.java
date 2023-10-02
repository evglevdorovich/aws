package com.example.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("db.payment-view")
public record PaymentViewDbConfigProperties(String tableName, String partitionKey) {

}
