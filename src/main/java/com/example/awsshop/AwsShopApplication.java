package com.example.awsshop;

import com.example.awsshop.repository.ProductRepositoryFileIO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AwsShopApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(AwsShopApplication.class, args);
    }

}
