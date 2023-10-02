package com.example.order.repository;

import com.example.order.config.OrderDbConfigProperties;
import com.example.order.model.Order;
import com.example.order.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryDynamo implements OrderRepository {
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final DynamoDbClient dynamoDbClient;
    private final OrderDbConfigProperties orderDbConfigProperties;
    @Value("${db.order.userIdIndex}")
    private String userIdIndex;

    @Override
    public Order upsert(Order order) {
        var putRequest = PutItemEnhancedRequest.builder(Order.class)
                .item(order)
                .build();
        dynamoDbEnhancedClient.table(orderDbConfigProperties.tableName(), TableSchema.fromBean(Order.class))
                .putItem(putRequest);
        return order;
    }

    @Override
    public List<Order> findOrderByUserId(String userId) {
        var userIndex = dynamoDbEnhancedClient
                .table(orderDbConfigProperties.tableName(), TableSchema.fromBean(Order.class))
                .index(userIdIndex);
        var userAttr = AttributeValue.builder()
                .s(userId)
                .build();

        var queryConditional = QueryConditional.keyEqualTo(
                Key.builder()
                        .partitionValue(userAttr)
                        .build());
        var results = userIndex.query(QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .limit(10)
                .build());
        var orders = results.stream()
                .flatMap(page -> page.items()
                        .stream())
                .toList();

        return orders;
    }

    @Override
    public Optional<Order> findById(String orderId) {
        var key = Key.builder().partitionValue(orderId).build();
        var optionalOrder = dynamoDbEnhancedClient.table(orderDbConfigProperties.tableName(), TableSchema.fromBean(Order.class))
                .getItem(key);
        return Optional.ofNullable(optionalOrder);
    }

    @Override
    public void updateWarehouseStatus(String orderId, OrderStatus orderStatus) {
        var itemKey = getPartitionKey(orderId);
        var warehouseStatusAttributeName = orderDbConfigProperties.warehouseStatusAttributeName();

        var updatedValues = getAttributeUpdatesForStatus(orderStatus, warehouseStatusAttributeName);

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(orderDbConfigProperties.tableName())
                .key(itemKey)
                .attributeUpdates(updatedValues)
                .build();

        dynamoDbClient.updateItem(request);
    }

    @Override
    public void updatePaymentStatus(String orderId, OrderStatus orderStatus) {
        var itemKey = getPartitionKey(orderId);
        var paymentStatusAttributeName = orderDbConfigProperties.paymentStatusAttributeName();

        var updatedValues = getAttributeUpdatesForStatus(orderStatus, paymentStatusAttributeName);

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(orderDbConfigProperties.tableName())
                .key(itemKey)
                .attributeUpdates(updatedValues)
                .build();

        dynamoDbClient.updateItem(request);
    }

    private HashMap<String, AttributeValueUpdate> getAttributeUpdatesForStatus(OrderStatus orderStatus, String attributeName) {
        var updatedValues = new HashMap<String, AttributeValueUpdate>();
        updatedValues.put(attributeName, AttributeValueUpdate.builder()
                .value(val -> val.s(orderStatus.name()))
                .action(AttributeAction.PUT)
                .build());
        return updatedValues;
    }

    private HashMap<String, AttributeValue> getPartitionKey(String orderId) {
        var itemKey = new HashMap<String, AttributeValue>();
        var partitionKeyValue = AttributeValue.builder().s(orderId).build();
        itemKey.put(orderDbConfigProperties.partitionKey(), partitionKeyValue);
        return itemKey;
    }

}
