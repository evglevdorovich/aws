package com.example.awsshop.controller;

import com.example.awsshop.dto.WarehouseProductDto;
import com.example.awsshop.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    @GetMapping("/warehouse")
    public String getWarehouse(Model model) {
        var warehouseProducts = warehouseService.findAll();
        model.addAttribute("warehouseProducts", warehouseProducts);
        return "warehouse";
    }

    @ResponseBody
    @PatchMapping("/warehouse")
    public void changeAmountForId(@RequestBody WarehouseProductDto warehouseProductDto) {
        warehouseService.changeAmountForId(warehouseProductDto);
    }
}
