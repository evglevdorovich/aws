package com.example.awsshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class UserVirtualPaymentAccount {

    @Id
    private Long id;
    private BigDecimal amountOfMoney;

}
