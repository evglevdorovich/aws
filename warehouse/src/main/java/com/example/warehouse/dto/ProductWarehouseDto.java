package com.example.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWarehouseDto {
    private String id;
    private String name;
    private Long quantity;

    public ProductWarehouseDto(String id, Long quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
