package com.example.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("queue")
public record QueueConfigProperties(String name, Integer waitTimeSeconds,
                                    Integer maxNumberOfMessages) {
}
