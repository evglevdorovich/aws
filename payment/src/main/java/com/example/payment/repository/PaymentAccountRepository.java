package com.example.payment.repository;


import com.example.payment.model.PaymentAccountLog;
import com.example.payment.model.PaymentAccountView;

public interface PaymentAccountRepository {

    PaymentAccountView getViewById(String userId);
    void updatePaymentAccountWithView(PaymentAccountLog paymentAccountLog, PaymentAccountView paymentAccountView);
}
