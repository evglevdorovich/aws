package com.example.awsshop.controller;

import com.example.awsshop.model.Product;
import com.example.awsshop.model.SelectedProduct;
import com.example.awsshop.model.ShoppingCart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCart shoppingCart;

    @GetMapping("/shoppingCarts")
    public String getShoppingCartProducts(Model model) {
        model.addAttribute("selectedProducts", shoppingCart.getSelectedProducts());
        model.addAttribute("shippingAddress", shoppingCart.getShippingAddress());
        model.addAttribute("totalAmount", shoppingCart.getTotalPrice());
        return "shoppingCart";
    }

    @GetMapping("/shoppingCarts/shippingAddresses")
    @ResponseBody
    public String getShoppingCartShippingAddresses() {
        return shoppingCart.getShippingAddress();
    }

    @ResponseBody
    @PostMapping("/shoppingCarts/products")
    public void createProductForShoppingCart(@RequestBody Product product) {
        var selectedProducts = shoppingCart.getSelectedProducts();

        var optionalSelectedProduct = selectedProducts
                .stream()
                .filter(selectedProduct -> selectedProduct.getProduct().equals(product))
                .findFirst();

        increaseQuantityOfProduct(optionalSelectedProduct, selectedProducts, product);
    }

    @ResponseBody
    @PostMapping("/shoppingCarts/shippingAddresses")
    public void createShippingAddressForShoppingCart(@RequestBody String serviceAddress) {
        shoppingCart.setShippingAddress(serviceAddress);
    }


    private static void increaseQuantityOfProduct(Optional<SelectedProduct> optionalSelectedProduct,
                                                  Set<SelectedProduct> selectedProducts, Product product) {
        if (optionalSelectedProduct.isPresent()) {
            var selectedProduct = optionalSelectedProduct.get();
            selectedProduct.setQuantity(selectedProduct.getQuantity() + 1);
        }
        else {
            selectedProducts.add(new SelectedProduct(product, 1L));
        }
    }

}
