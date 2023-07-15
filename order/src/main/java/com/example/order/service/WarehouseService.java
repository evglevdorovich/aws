package com.example.order.service;

import com.example.order.config.WarehouseNotificationServiceConfig;
import com.example.order.dto.WarehouseDto;
import com.example.order.dto.WarehouseView;
import com.example.order.exception.PaymentFormatException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {
    private final RestTemplate restTemplate;
    //withdrawal
    private final SnsClient snsClient;
    private final ObjectWriter objectWriter;
    private final WarehouseNotificationServiceConfig notificationServiceConfig;
    private final ProductService productService;
    //hardcoded
    private final static String WAREHOUSE_URL = "http://warehouse:8082/products";

    public void sendProducts(WarehouseDto warehouseDto) {
        var warehouseDtoJson = getWarehouseDtoJson(warehouseDto);
        var messageGroupId = warehouseDto.getOrderId();
        var deduplicationId = warehouseDto.getOrderId();
        var warehousePublishRequest = PublishRequest.builder()
                .message(warehouseDtoJson)
                .messageGroupId(messageGroupId)
                .messageDeduplicationId(deduplicationId)
                .topicArn(notificationServiceConfig.topicArn())
                .build();

        snsClient.publish(warehousePublishRequest);
    }

    public List<WarehouseView> getProducts(){
        return Arrays.asList(restTemplate
                .getForObject(URI.create(WAREHOUSE_URL), WarehouseView[].class));
    }

    private String getWarehouseDtoJson(WarehouseDto warehouseDto) {
        try {
            return objectWriter.writeValueAsString(warehouseDto);
        } catch (JsonProcessingException e) {
            log.warn("fail to process json with ordered products: {}", e.getMessage());
            throw new PaymentFormatException(e.getMessage());
        }
    }
}
