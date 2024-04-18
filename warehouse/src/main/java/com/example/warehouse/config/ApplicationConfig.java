package com.example.warehouse.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialRandomBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class ApplicationConfig {
    @Value("${retry.maxAttempts}")
    private Integer maxAttempts;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ObjectWriter objectWriter(ObjectMapper objectMapper) {
        return objectMapper.writer();
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        var randomBackOffPolicy = new ExponentialRandomBackOffPolicy();
        retryTemplate.setBackOffPolicy(randomBackOffPolicy);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(maxAttempts);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("**").allowedOrigins("http://order:8080")
                        .allowedMethods("PATCH", "OPTIONS");
            }
        };
    }

}
