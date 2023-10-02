package com.example.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("postfix.payment")
public record PaymentPostfixConfig(String deposit, String cancel, String receipt) {

}
