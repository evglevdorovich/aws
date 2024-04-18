package com.example.payment.validator;

import com.example.payment.exception.NotEnoughMoneyOnTheAccountException;
import com.example.payment.model.PaymentAccountLog;
import com.example.payment.model.PaymentAccountView;
import com.example.payment.utils.PaymentLogReasonChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentValidator {
    private final PaymentLogReasonChecker paymentLogReasonChecker;

    public void validatePayment(PaymentAccountLog paymentAccountLog,
                                   PaymentAccountView paymentAccountView) {
        if (paymentLogReasonChecker.isReceipt(paymentAccountLog)) {
            validateReceipt(paymentAccountLog, paymentAccountView);
        }
    }

    private void validateReceipt(PaymentAccountLog paymentAccountLog, PaymentAccountView paymentAccountView) {
        var userTotalAmount = paymentAccountView.getMoneyAmount();
        var amountToDiff = paymentAccountLog.getAmountToDiff();
        var totalAmountAfterReceipt = userTotalAmount.add(amountToDiff);

        if (totalAmountAfterReceipt.compareTo(BigDecimal.ZERO) < 0) {
            log.warn("user with id = {} and orderId = {}, has insufficient funds", paymentAccountLog.getUserId(),
                    paymentAccountLog.getOrderId());
            throw new NotEnoughMoneyOnTheAccountException();
        }
    }
}
