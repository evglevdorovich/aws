package com.example.order.utils;

import com.example.order.model.OrderResult;
import com.example.order.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderReasonExtractorTest {

    private OrderReasonExtractor orderReasonExtractor;

    @BeforeEach
    public void setup(){
        orderReasonExtractor = new OrderReasonExtractor();
    }
    @Test
    void test(){
        var result = new OrderResult("fosfgj_withrow", OrderStatus.SUCCESS, "warehouse");
        assertThat(orderReasonExtractor.extractReason(result))
                .isEqualTo(new OrderResult("fosfgj", OrderStatus.SUCCESS, "warehouse"));
    }

}