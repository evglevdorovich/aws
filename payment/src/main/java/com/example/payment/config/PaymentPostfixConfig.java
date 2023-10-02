package com.example.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("payment.postfix")
public record PaymentPostfixConfig(String deposit, String cancel, String receipt) {

}
