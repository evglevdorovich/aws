package com.example.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("queue.replace-notifier")
public record ReplaceNotifierQueueConfigProperties(String name, Integer waitTimeSeconds,
                                                   Integer maxNumberOfMessages) {
}
