package com.example.payment.repository;

import com.example.payment.config.PaymentDbConfigProperties;
import com.example.payment.config.PaymentViewDbConfigProperties;
import com.example.payment.model.PaymentAccountLog;
import com.example.payment.model.PaymentAccountView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;

@RequiredArgsConstructor
@Repository
public class PaymentAccountRepositoryDynamo implements PaymentAccountRepository {
    private final DynamoDbEnhancedClient dynamoDbClient;
    private final PaymentViewDbConfigProperties paymentViewDbConfigProperties;
    private final PaymentDbConfigProperties paymentDbConfigProperties;

    public PaymentAccountView getViewById(String userId) {

        var paymentAccountViewTable = getPaymentAccountViewDynamoDbTable();

        var paymentPartitionKey = Key.builder()
                .partitionValue(userId)
                .build();

        return paymentAccountViewTable.getItem(paymentPartitionKey);
    }

    @Override
    public void updatePaymentAccountWithView(PaymentAccountLog paymentAccountLog, PaymentAccountView paymentAccountView) {
        var paymentAccountTable = getPaymentAccountLogDynamoDbTable();
        var paymentAccountViewTable = getPaymentAccountViewDynamoDbTable();

        var paymentTransactWriteRequests = TransactWriteItemsEnhancedRequest.builder()
                .addPutItem(paymentAccountTable, paymentAccountLog)
                .addPutItem(paymentAccountViewTable, paymentAccountView)
                .build();

        dynamoDbClient.transactWriteItems(paymentTransactWriteRequests);
    }

    private DynamoDbTable<PaymentAccountView> getPaymentAccountViewDynamoDbTable() {
        var paymentAccountViewSchema = TableSchema.fromBean(PaymentAccountView.class);
        return dynamoDbClient
                .table(paymentViewDbConfigProperties.tableName(), paymentAccountViewSchema);
    }

    private DynamoDbTable<PaymentAccountLog> getPaymentAccountLogDynamoDbTable() {
        var paymentAccountViewSchema = TableSchema.fromBean(PaymentAccountLog.class);
        return dynamoDbClient
                .table(paymentDbConfigProperties.tableName(), paymentAccountViewSchema);
    }

}
