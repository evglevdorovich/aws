package com.example.warehouse.model;

import com.example.warehouse.status.WarehouseEventStatus;
import lombok.Builder;

@Builder
public record WarehouseEventResult(String orderId, WarehouseEventStatus status) {
}