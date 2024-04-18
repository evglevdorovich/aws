package com.example.warehouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WarehouseViewLogComposition {
    private WarehouseLog warehouseLog;
    private WarehouseView warehouseView;
}
