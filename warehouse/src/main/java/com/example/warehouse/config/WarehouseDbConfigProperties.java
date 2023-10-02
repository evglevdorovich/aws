package com.example.warehouse.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("db.warehouse")
public record WarehouseDbConfigProperties(String tableName, String partitionKey, String sortKey) {

}
