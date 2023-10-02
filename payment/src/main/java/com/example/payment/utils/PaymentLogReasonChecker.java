package com.example.payment.utils;

import com.example.payment.config.PaymentPostfixConfig;
import com.example.payment.model.PaymentAccountLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentLogReasonChecker {
    private final PaymentPostfixConfig paymentPostfixConfig;

    public boolean isReceipt(PaymentAccountLog paymentAccountLog) {
        var orderId = paymentAccountLog.getOrderId();
        var postfix = paymentPostfixConfig.receipt();
        return orderId.endsWith(postfix);
    }

}
