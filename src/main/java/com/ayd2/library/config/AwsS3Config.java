package com.ayd2.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class AwsS3Config {

    private final AwsProperties awsProperties;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCredentials =
            AwsBasicCredentials.create(
                awsProperties.getClient().getAccessKeyId(), 
                awsProperties.getClient().getSecretAccessKey());

        // Builder del cliente S3
        return S3Client.builder()
                .region(Region.of(awsProperties.getS3().getBucketRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }
}