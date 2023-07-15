package com.example.warehouse.controller;

import com.example.warehouse.dto.ProductWarehouseDto;
import com.example.warehouse.dto.WarehouseChangeDto;
import com.example.warehouse.dto.WarehouseDto;
import com.example.warehouse.model.WarehouseView;
import com.example.warehouse.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    @GetMapping("/products")
    public List<WarehouseView> warehouseViews() {
        return warehouseService.warehouseViews();
    }

    @PatchMapping("/products/{productId}")
    @CrossOrigin
    public ResponseEntity<String> warehouseViews(@PathVariable String productId,
                                                 @RequestBody WarehouseChangeDto warehouseChangeDto) {
        var productWarehouseDto = new ProductWarehouseDto(productId, warehouseChangeDto.quantity());
        var warehouseDto = new WarehouseDto(warehouseChangeDto.orderId(), List.of(productWarehouseDto));
        warehouseService.sendWarehouseDto(warehouseDto);
        return ResponseEntity.ok().build();
    }
}
