package com.example.awsshop.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
