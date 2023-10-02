package com.example.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("warehouse")
public record WarehouseConfig(int productTypeLimit) {
}
