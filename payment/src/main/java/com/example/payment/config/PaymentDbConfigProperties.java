package com.example.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("db.payment")
public record PaymentDbConfigProperties(String tableName, String partitionKey, String sortKey) {

}
