package com.example.order.utils;

import com.example.order.config.PaymentPostfixConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentOrderIdReasonModifier {
    private final PaymentPostfixConfig paymentPostfixConfig;
    public String modifyForReceipt(String orderId) {
        return orderId + paymentPostfixConfig.receipt();
    }

    public String modifyForCancel(String orderId) {
        return orderId + paymentPostfixConfig.cancel();
    }
}
