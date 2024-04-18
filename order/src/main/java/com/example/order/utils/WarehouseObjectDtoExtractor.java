package com.example.order.utils;

import com.example.order.dto.WarehouseDto;
import com.example.order.dto.ProductWarehouseDto;
import com.example.order.model.Order;
import com.example.order.validator.WarehouseDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WarehouseObjectDtoExtractor {
    private final WarehouseOrderIdReasonModifier warehouseOrderIdReasonModifier;
    private final ObjectMapperCollectionUtils mapper;
    private final WarehouseDtoValidator warehouseDtoValidator;
    public WarehouseDto extractFromOrderForWithdrawal(Order order){
        var paymentOrderId = warehouseOrderIdReasonModifier.modifyForWithdrawal(order.getId());
        WarehouseDto warehouseDto = extractFromOrderWithModifiedOrderId(order, paymentOrderId);
        warehouseDtoValidator.validation(warehouseDto);
        return warehouseDto;
    }

    public WarehouseDto extractFromOrderForCancel(Order order){
        var paymentOrderId = warehouseOrderIdReasonModifier.modifyForCancel(order.getId());
        return extractFromOrderWithModifiedOrderId(order, paymentOrderId);
    }

    private WarehouseDto extractFromOrderWithModifiedOrderId(Order order, String paymentOrderId) {
        var warehouseProducts = mapper.mapAllToList(order.getProducts(), ProductWarehouseDto.class);
        return new WarehouseDto(paymentOrderId, warehouseProducts);
    }
}
