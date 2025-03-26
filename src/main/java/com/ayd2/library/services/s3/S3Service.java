package com.ayd2.library.services.s3;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String uploadFile(String baseFileName, MultipartFile file);
}
