package com.example.awsshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AwsShopApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(AwsShopApplication.class, args);
    }

}
