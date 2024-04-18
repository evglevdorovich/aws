package com.example.payment.controller;

import com.example.payment.dto.PaymentLogDto;
import com.example.payment.model.PaymentAccountLog;
import com.example.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PatchMapping("/paymentAccount/{userId}")
    public ResponseEntity<String> sendMoney(@RequestBody PaymentLogDto paymentLogDto, @PathVariable String userId) {
        var paymentAccountLog = new PaymentAccountLog(userId, paymentLogDto.orderId(), paymentLogDto.moneyToDeposit());
        paymentService.sendPayment(paymentAccountLog);
        return ResponseEntity.ok().build();
    }
}
