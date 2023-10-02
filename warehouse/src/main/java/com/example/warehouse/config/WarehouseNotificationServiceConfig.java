package com.example.warehouse.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("notification-service.warehouse")
public record WarehouseNotificationServiceConfig(String topicArn) {
}
