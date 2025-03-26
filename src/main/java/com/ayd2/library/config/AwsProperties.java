package com.ayd2.library.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aws")
@Getter
@Setter
public class AwsProperties {

    private final S3 s3 = new S3();
    private final Client client = new Client();

    @Getter
    @Setter
    public static class S3 {
        private String bucketName;
        private String bucketRegion;
    }

    @Getter
    @Setter
    public static class Client {
        private String accessKeyId;
        private String secretAccessKey;
    }
}

