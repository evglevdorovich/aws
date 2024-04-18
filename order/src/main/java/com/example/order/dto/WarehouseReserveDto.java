package com.example.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseReserveDto {
    private String orderId;
    private Long quantity;
}
