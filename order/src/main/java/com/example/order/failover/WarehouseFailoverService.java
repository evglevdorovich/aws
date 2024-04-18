package com.example.order.failover;

import com.example.order.model.Order;
import com.example.order.service.WarehouseService;
import com.example.order.utils.WarehouseObjectDtoExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WarehouseFailoverService {
    private final WarehouseService warehouseService;
    private final WarehouseObjectDtoExtractor objectDtoExtractor;
    public void onFail(Order order) {
        var warehouseDto = objectDtoExtractor.extractFromOrderForCancel(order);
        warehouseService.sendProducts(warehouseDto);
    }
}
