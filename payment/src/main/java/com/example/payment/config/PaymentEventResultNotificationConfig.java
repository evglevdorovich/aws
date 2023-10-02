package com.example.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("event-result-notification")
public record PaymentEventResultNotificationConfig(String topicArn) {
}
