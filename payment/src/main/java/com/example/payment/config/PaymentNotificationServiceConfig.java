package com.example.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("notification-service.payments")
public record PaymentNotificationServiceConfig(String topicArn) {
}
