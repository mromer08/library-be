package com.ayd2.library.services.s3;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.exception.SdkException;

public interface S3Service {
    String uploadFile(String baseFileName, MultipartFile file) throws SdkException, IOException;

}
