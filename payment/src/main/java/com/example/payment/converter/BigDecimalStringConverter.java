package com.example.payment.converter;

import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.math.BigDecimal;

public class BigDecimalStringConverter implements AttributeConverter<BigDecimal> {
    @Override
    public AttributeValue transformFrom(BigDecimal bigDecimal) {
        return AttributeValue.builder().s(bigDecimal.toString()).build();
    }

    @Override
    public BigDecimal transformTo(AttributeValue attributeValue) {
        return new BigDecimal(attributeValue.s());
    }

    @Override
    public EnhancedType<BigDecimal> type() {
        return EnhancedType.of(BigDecimal.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.N;
    }
}
