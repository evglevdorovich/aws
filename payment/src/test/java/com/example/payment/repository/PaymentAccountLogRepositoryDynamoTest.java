package com.example.payment.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PaymentAccountLogRepositoryDynamoTest {

    @Autowired
    private PaymentAccountRepository paymentAccountRepository;

    @Test
    void getById() {
        paymentAccountRepository.getViewById("1");
    }
}