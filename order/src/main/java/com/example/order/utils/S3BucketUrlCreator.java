package com.example.order.utils;

import org.springframework.stereotype.Component;

@Component
public class S3BucketUrlCreator {
    public String create(String bucketLocation, String bucketName) {
        bucketLocation = addDotForNotEmptyBucket(bucketLocation);
        return String.format("http://%s.s3.%samazonaws.com/",bucketName, bucketLocation);
    }

    private static String addDotForNotEmptyBucket(String s3BucketLocation) {
        if (!s3BucketLocation.isEmpty()) {
            s3BucketLocation = s3BucketLocation + ".";
        }
        return s3BucketLocation;
    }
}
