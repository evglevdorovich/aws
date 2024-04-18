package com.example.order.dto;

import java.math.BigDecimal;

public record PaymentDepositDto(String orderId, BigDecimal moneyToDeposit) {
}
