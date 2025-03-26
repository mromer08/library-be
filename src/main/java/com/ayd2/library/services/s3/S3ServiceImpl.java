package com.ayd2.library.services.s3;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ayd2.library.config.AwsProperties;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final AwsProperties awsProperties;
    private final S3Client s3Client;
    private final List<String> allowedImageTypes = Arrays.asList("image/jpeg", "image/png", "image/gif", "image/jpg");

    @Override
    public String uploadFile(String baseFileName, MultipartFile file) throws SdkException, IOException {
            String contentType = file.getContentType();
            if (!allowedImageTypes.contains(contentType)) {
                throw new IllegalArgumentException("Solo se permiten archivos de tipo imagen (JPEG, PNG, GIF, JPG)");
            }

            String fileExtension = Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf("."));
            String finalFileName = baseFileName + fileExtension;
            System.out.println("Uploading file: " + finalFileName);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(awsProperties.getS3().getBucketName())
                    .key(finalFileName)
                    .contentType(contentType)
                    .build();

            PutObjectResponse s3ObjectResponse = s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromInputStream(
                    file.getInputStream(), file.getSize()));

            System.out.println(s3ObjectResponse.sdkHttpResponse());
            return finalFileName;
    }
}
