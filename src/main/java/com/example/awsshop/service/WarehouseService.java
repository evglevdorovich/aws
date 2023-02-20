package com.example.awsshop.service;

import com.example.awsshop.dto.WarehouseProductDto;
import com.example.awsshop.exception.NotEnoughAmountOfProductsOnWarehouse;
import com.example.awsshop.exception.NotSufficientProductsInWarehouseException;
import com.example.awsshop.exception.WarehouseProductNotFoundException;
import com.example.awsshop.model.ShoppingCart;
import com.example.awsshop.model.WarehouseProduct;
import com.example.awsshop.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    @Transactional
    public void reserveProductsFromWarehouse(ShoppingCart shoppingCart) {

        for (var selectedProduct : shoppingCart.getSelectedProducts()) {

            var productId = selectedProduct.getProduct().getId();
            var warehouseProduct = warehouseRepository.findByProductId(productId).orElseThrow(WarehouseProductNotFoundException::new);
            var amount = selectedProduct.getQuantity();

            var warehouseProductAmount = warehouseProduct.getAmount();
            if (warehouseProductAmount < amount) {
                throw new NotSufficientProductsInWarehouseException("current amount is " + warehouseProduct.getAmount());
            }
            warehouseProduct.setAmount(warehouseProductAmount - amount);

            warehouseRepository.save(warehouseProduct);
        }
    }

    @Transactional
    public List<WarehouseProduct> findAll() {
        return warehouseRepository.findAll();
    }

    @Transactional
    public void changeAmountForId(WarehouseProductDto warehouseProductDto) {
        var warehouseProduct = warehouseRepository.findById(warehouseProductDto.productId()).orElseThrow(WarehouseProductNotFoundException::new);
        var changedAmount = warehouseProductDto.changedAmount();
        if (changedAmount < 0 && Math.abs(changedAmount) > warehouseProduct.getAmount()) {
            throw new NotEnoughAmountOfProductsOnWarehouse();
        }
        changeAmount(changedAmount, warehouseProduct);
    }

    private void changeAmount(Long changedAmount, WarehouseProduct warehouseProduct) {
        warehouseProduct.setAmount(warehouseProduct.getAmount() + changedAmount);
    }

}
