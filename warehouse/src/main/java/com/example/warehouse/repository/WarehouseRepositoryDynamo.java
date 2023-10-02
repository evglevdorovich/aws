package com.example.warehouse.repository;

import com.example.warehouse.config.WarehouseDbConfigProperties;
import com.example.warehouse.config.WarehouseViewDbConfigProperties;
import com.example.warehouse.model.WarehouseLog;
import com.example.warehouse.model.WarehouseView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchGetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ReadBatch;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class WarehouseRepositoryDynamo implements WarehouseRepository {
    private final DynamoDbEnhancedClient dynamoDbClient;
    private final WarehouseViewDbConfigProperties warehouseViewDbConfigProperties;
    private final WarehouseDbConfigProperties warehouseDbConfigProperties;

    public WarehouseView getViewById(String userId) {

        var warehouseViewTable = getWarehouseViewDynamoDbTable();

        var paymentPartitionKey = getPartitionKey(userId);

        return warehouseViewTable.getItem(paymentPartitionKey);
    }

    @Override
    public Set<WarehouseView> getViewsByIds(List<String> productIds) {
        var warehouseViewTable = getWarehouseViewDynamoDbTable();
        var readBatches = productIds.stream()
                .map(mapIdToReadBatch(warehouseViewTable))
                .toList();

        BatchGetItemEnhancedRequest batchGetItemRequest = BatchGetItemEnhancedRequest.builder()
                .readBatches(readBatches)
                .build();

        // automatically do retry if has unprocessed keys
        return dynamoDbClient.batchGetItem(batchGetItemRequest)
                .resultsForTable(getWarehouseViewDynamoDbTable())
                .stream()
                .collect(Collectors.toSet());
    }

    @Override
    public void updateWarehouseWithViews(Set<WarehouseView> views, Set<WarehouseLog> logs) {
        var warehouseLogTable = getWarehouseLogDynamoDbTable();
        var warehouseViewTable = getWarehouseViewDynamoDbTable();

        var transactWriteBuilder = TransactWriteItemsEnhancedRequest.builder();

        views.forEach(view -> transactWriteBuilder.addPutItem(warehouseViewTable, view));
        logs.forEach(log -> transactWriteBuilder.addPutItem(warehouseLogTable, log));

        dynamoDbClient.transactWriteItems(transactWriteBuilder.build());
    }

    @Override
    public List<WarehouseView> getAll() {
        var warehouseViewTable = getWarehouseViewDynamoDbTable();
        return warehouseViewTable.scan().stream()
                .flatMap(page -> page.items()
                        .stream())
                .toList();
    }

    private DynamoDbTable<WarehouseView> getWarehouseViewDynamoDbTable() {
        var warehouseViewSchema = TableSchema.fromBean(WarehouseView.class);
        return dynamoDbClient
                .table(warehouseViewDbConfigProperties.tableName(), warehouseViewSchema);
    }

    private DynamoDbTable<WarehouseLog> getWarehouseLogDynamoDbTable() {
        var warehouseLogSchema = TableSchema.fromBean(WarehouseLog.class);
        return dynamoDbClient
                .table(warehouseDbConfigProperties.tableName(), warehouseLogSchema);
    }


    private static Function<String, ReadBatch> mapIdToReadBatch(DynamoDbTable<WarehouseView> warehouseViewTable) {
        return id -> ReadBatch.builder(WarehouseView.class)
                .mappedTableResource(warehouseViewTable)
                .addGetItem(getPartitionKey(id))
                .build();
    }

    private static Key getPartitionKey(String id) {
        return Key.builder().partitionValue(id).build();
    }

}
