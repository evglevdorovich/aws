package com.example.order.service;

import com.example.order.model.Product;
import com.example.order.model.SelectedProduct;
import com.example.order.utils.SelectedProductPriceCalculator;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Component
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCartService {
    private String shippingAddress;
    private Set<SelectedProduct> selectedProducts;
    private ModelMapper modelMapper;
    public ShoppingCartService(ModelMapper modelMapper) {
        shippingAddress = "";
        selectedProducts = new HashSet<>();
        this.modelMapper = modelMapper;
    }

    public BigDecimal getTotalPrice() {
        return selectedProducts.stream()
                .map(SelectedProductPriceCalculator::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addProduct(Product product) {

        var initialSelectedProduct = modelMapper.map(product, SelectedProduct.class);

        var addedSelectedProduct = selectedProducts
                .stream()
                .filter(selectedProduct -> selectedProduct.getId().equals(product.getId()))
                .findFirst()
                .orElse(initialSelectedProduct);

        addedSelectedProduct.incrementQuantity();
        selectedProducts.add(addedSelectedProduct);
    }

    public void clean() {
        setSelectedProducts(new HashSet<>());
    }

}
