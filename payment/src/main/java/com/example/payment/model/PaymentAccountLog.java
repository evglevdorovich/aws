package com.example.payment.model;

import com.example.payment.converter.BigDecimalStringConverter;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class PaymentAccountLog {
    @Getter(onMethod_={@DynamoDbPartitionKey})
    private String userId;
    @Getter(onMethod_={@DynamoDbSortKey})
    private String orderId;
    @Getter(onMethod = @__({@DynamoDbConvertedBy(BigDecimalStringConverter.class)}))
    private BigDecimal amountToDiff;

}