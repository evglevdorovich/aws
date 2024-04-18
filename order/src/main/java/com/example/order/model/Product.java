package com.example.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Product {
    private String id;
    private String title;
    private String description;
    private String photoUrl;
    private BigDecimal price;
    private List<Tag> tags;
}
