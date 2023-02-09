package com.example.awsshop;

import com.example.awsshop.repository.ProductRepositoryS3;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AwsShopApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(AwsShopApplication.class, args);
        run.getBean(ProductRepositoryS3.class).getAll();
    }

}
