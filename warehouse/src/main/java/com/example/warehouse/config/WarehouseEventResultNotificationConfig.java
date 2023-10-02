package com.example.warehouse.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("event-result-notification")
public record WarehouseEventResultNotificationConfig(String topicArn) {
}
