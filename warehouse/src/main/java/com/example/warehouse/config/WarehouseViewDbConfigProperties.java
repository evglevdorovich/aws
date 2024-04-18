package com.example.warehouse.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("db.warehouse-view")
public record WarehouseViewDbConfigProperties(String tableName, String partitionKey) {

}
