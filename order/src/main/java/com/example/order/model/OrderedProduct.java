package com.example.order.model;

import com.example.order.converter.BigDecimalStringConverter;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;

import java.math.BigDecimal;

@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderedProduct {
    private String id;
    private String name;
    @Getter(onMethod = @__({@DynamoDbConvertedBy(BigDecimalStringConverter.class)}))
    private BigDecimal price;
    private Long quantity;
}
