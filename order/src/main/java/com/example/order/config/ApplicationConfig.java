package com.example.order.config;

import com.example.order.model.OrderedProduct;
import com.example.order.model.SelectedProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {
        var mapper = new ModelMapper();
        addSelectedProductOrderedProductTypeMap(mapper);
        return mapper;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ObjectWriter objectWriter(ObjectMapper objectMapper) {
        return objectMapper.writer();
    }

    private static void addSelectedProductOrderedProductTypeMap(ModelMapper mapper) {
        var propertyMapper = mapper.createTypeMap(SelectedProduct.class, OrderedProduct.class);
        propertyMapper.addMapping(SelectedProduct::getTitle, OrderedProduct::setName);
    }

}
