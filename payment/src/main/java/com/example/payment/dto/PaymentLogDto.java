package com.example.payment.dto;

import java.math.BigDecimal;

public record PaymentLogDto(String orderId, BigDecimal moneyToDeposit) {
}
