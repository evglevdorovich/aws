package com.example.order.failover;

import com.example.order.model.Order;
import com.example.order.service.PaymentService;
import com.example.order.utils.PaymentObjectDtoExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentFailoverService {
    private final PaymentService paymentService;
    private final PaymentObjectDtoExtractor objectDtoExtractor;
    public void onFail(Order order) {
        var paymentDto = objectDtoExtractor.extractFromOrderForCancel(order);
        paymentService.sendPayment(paymentDto);
    }
}
