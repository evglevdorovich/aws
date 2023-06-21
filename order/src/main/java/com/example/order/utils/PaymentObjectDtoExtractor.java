package com.example.order.utils;

import com.example.order.dto.PaymentDto;
import com.example.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentObjectDtoExtractor {

    private final PaymentOrderIdReasonModifier paymentOrderIdReasonModifier;
    public PaymentDto extractFromOrderForWithdrawal(Order order){
        var paymentOrderId = paymentOrderIdReasonModifier.modifyForReceipt(order.getId());
        var amountToDiff = order.getTotalAmount().negate();
        return new PaymentDto(paymentOrderId, order.getUserId(), amountToDiff);
    }

    public PaymentDto extractFromOrderForCancel(Order order){
        var paymentOrderId = paymentOrderIdReasonModifier.modifyForCancel(order.getId());
        return new PaymentDto(paymentOrderId, order.getUserId(), order.getTotalAmount());
    }
}
