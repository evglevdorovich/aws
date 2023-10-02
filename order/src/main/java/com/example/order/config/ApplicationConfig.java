package com.example.order.config;

import com.example.order.model.OrderedProduct;
import com.example.order.model.SelectedProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

    private static void addSelectedProductOrderedProductTypeMap(ModelMapper mapper) {
        var propertyMapper = mapper.createTypeMap(SelectedProduct.class, OrderedProduct.class);
        propertyMapper.addMapping(SelectedProduct::getTitle, OrderedProduct::setName);
    }

    @Bean
    public ModelMapper modelMapper() {
        var mapper = new ModelMapper();
        addSelectedProductOrderedProductTypeMap(mapper);
        return mapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        var requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ObjectWriter objectWriter(ObjectMapper objectMapper) {
        return objectMapper.writer();
    }

}
