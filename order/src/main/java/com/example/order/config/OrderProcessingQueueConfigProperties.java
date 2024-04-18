package com.example.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("queue.order-processing")
public record OrderProcessingQueueConfigProperties(String name, Integer waitTimeSeconds,
                                                   Integer maxNumberOfMessages) {
}
