package com.example.warehouse.model;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbVersionAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class WarehouseView {
    @Getter(onMethod_={@DynamoDbPartitionKey})
    private String productId;
    private Long quantity;
    @Getter(onMethod_={@DynamoDbVersionAttribute})
    private Long version;

    public WarehouseView(String productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarehouseView that = (WarehouseView) o;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    public void addQuantity(Long quantity) {
        this.quantity += quantity;
    }
}
