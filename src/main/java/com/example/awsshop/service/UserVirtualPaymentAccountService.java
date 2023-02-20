package com.example.awsshop.service;

import com.example.awsshop.exception.NotEnoughMoneyOnTheAccountException;
import com.example.awsshop.exception.UserVirtualPaymentAccountNotFoundException;
import com.example.awsshop.model.UserVirtualPaymentAccount;
import com.example.awsshop.repository.UserVirtualPaymentAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserVirtualPaymentAccountService {

    private final UserVirtualPaymentAccountRepository userVirtualPaymentAccountRepository;


    @Transactional
    public UserVirtualPaymentAccount save(UserVirtualPaymentAccount userVirtualPaymentAccount) {
        return userVirtualPaymentAccountRepository.save(userVirtualPaymentAccount);
    }
    @Transactional
    public void decreaseMoneyById(Double amount, Long userId) {
        var userVirtualPaymentAccount = userVirtualPaymentAccountRepository.findById(userId)
                .orElseThrow(UserVirtualPaymentAccountNotFoundException::new);
        var amountDecreased = BigDecimal.valueOf(amount);
        if (amountDecreased.compareTo(userVirtualPaymentAccount.getAmountOfMoney()) > 0) {
            throw new NotEnoughMoneyOnTheAccountException();
        }
        userVirtualPaymentAccount.setAmountOfMoney(userVirtualPaymentAccount.getAmountOfMoney()
                .subtract(amountDecreased));
    }
}
