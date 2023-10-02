package com.example.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("postfix.warehouse")
public record WarehousePostfixConfig(String deposit, String cancel, String withdrawal) {

}
