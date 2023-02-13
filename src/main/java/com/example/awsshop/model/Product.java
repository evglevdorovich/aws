package com.example.awsshop.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class Product {
    private Long id;
    private String title;
    private String description;
    private String photoUrl;
    private String price;
    private List<String> tags;
}
