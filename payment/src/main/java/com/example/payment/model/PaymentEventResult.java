package com.example.payment.model;

import com.example.payment.status.PaymentStatus;
import lombok.Builder;

@Builder
public record PaymentEventResult(String orderId, PaymentStatus status) {

}
