package com.example.order.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectedProduct {

    private String id;
    private long quantity;
    private BigDecimal price;
    private String title;

    public void incrementQuantity(){
        quantity++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectedProduct that = (SelectedProduct) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
