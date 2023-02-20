package com.example.awsshop.config;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {
//
//    @Bean
//    public AWSCredentials credentials() {
//        try(var credentialsProvider = new InstanceProfileCredentialsProvider(true)) {
//            return credentialsProvider.getCredentials();
//        } catch (IOException e) {
//            throw new CannotFindIamRoleCredentials();
//        }
//    }

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new InstanceProfileCredentialsProvider(false))
                .withRegion(Regions.US_EAST_1)
                .build();
    }
}