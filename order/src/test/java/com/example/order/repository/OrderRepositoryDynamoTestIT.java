package com.example.order.repository;

import com.example.order.model.*;
import com.example.order.utils.ObjectMapperCollectionUtils;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("dev")
@TestPropertySource("classpath:application-test.yaml")
class OrderRepositoryDynamoTestIT {
    @Autowired
    private OrderRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ObjectMapperCollectionUtils objectMapperCollectionUtils;
    @Test
    void putOrder() {
        var productOrdered = OrderedProduct.builder()
                .id(UUID.randomUUID().toString())
                .quantity(5L)
                .name("someNAme")
                .price(new BigDecimal("100"))
                .build();
        var order = Order.builder()
                .id(UUID.randomUUID().toString())
                .totalAmount(new BigDecimal("100000"))
                .createdDate(Instant.now())
                .shippingAddress("someWhere")
                .warehouseStatus(OrderStatus.IN_PROGRESS)
                .products(List.of(productOrdered))
                .build();
        var actualOrdered = repository.upsert(order);
    }

    @Test
    void updateOrder() {
        repository.updateWarehouseStatus("dca7ff2a-87b2-4bc9-9a21-9b8fa8e6b8d0", OrderStatus.SUCCESS);
        System.out.println();
    }
    @Test
    void findById() {
        repository.findById("don't exist");
        repository.findById("dca7ff2a-87b2-4bc9-9a21-9b8fa8e6b8d0");
    }

    @Test
    void findByOrderId() {
        repository.findOrderByUserId("-1");
        System.out.println();
    }

    @Test
    void testConvert() {
        var product = new Product("1", "title", "descr", "url", BigDecimal.TEN, List.of(new Tag("tag")));
        var selectedProduct = new SelectedProduct("5", 5L,BigDecimal.TEN, "name");
        var objects = objectMapperCollectionUtils.mapAllToSet(Set.of(selectedProduct), OrderedProduct.class);
        var model = modelMapper.map(selectedProduct, OrderedProduct.class);
        var model2 = modelMapper.map(new Product("1", "title", "descr", "photoUrl", BigDecimal.TEN,null), SelectedProduct.class);
    }
}