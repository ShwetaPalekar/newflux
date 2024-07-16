package com.hk.gateway.springgateway.services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;

@Service
public class S3Service {

    private static final String AWS_ACCESS_KEY = "your-access-key";
    private static final String AWS_SECRET_KEY = "your-secret-key";
    private static final String S3_BUCKET_NAME = "your-bucket-name";
    private static final String AWS_REGION = "your-region";

    private final AmazonS3 s3Client;

    public S3Service() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(AWS_REGION)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    public Flux<InputStream> downloadFromS3(Flux<String> objectKeys) {
        return objectKeys.flatMap(this::downloadSingleFromS3);
    }

    private Mono<InputStream> downloadSingleFromS3(String objectKey) {
        return Mono.fromCallable(() -> {
            S3Object s3Object = s3Client.getObject(S3_BUCKET_NAME, objectKey);
            return s3Object.getObjectContent();
        });
    }
}
