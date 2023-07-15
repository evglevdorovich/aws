package com.example.order.controller;

import com.example.order.dto.WarehouseReserveDto;
import com.example.order.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;
    private final RestTemplate restTemplate;
    private final String URL_TO_WAREHOUSE = "http://warehouse:8082/products/%s";

    // just hardcoding

    @GetMapping("/warehouse")
    public String getWarehouse(Model model) {
        var warehouseViews = warehouseService.getProducts();
        model.addAttribute("warehouseProducts", warehouseViews);
        return "warehouse";
    }

    //TODO: move to services etc
    @PatchMapping("/products/{productId}")
    @ResponseBody
    public ResponseEntity<String> sendWarehouse(@RequestBody WarehouseReserveDto reserveDto, @PathVariable String productId){
        restTemplate.patchForObject(String.format(URL_TO_WAREHOUSE, productId),
                reserveDto, WarehouseReserveDto.class);
        return ResponseEntity.ok().build();
    }
}
