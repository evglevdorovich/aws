package com.example.warehouse.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("warehouse.postfix")
public record WarehousePostfixConfig(String deposit, String cancel, String withdrawal) {

}
