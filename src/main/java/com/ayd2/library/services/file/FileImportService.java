package com.ayd2.library.services.file;

import org.springframework.web.multipart.MultipartFile;

import com.ayd2.library.dto.file.FileImportResponseDTO;

public interface FileImportService {
    FileImportResponseDTO importData(MultipartFile file) throws Exception;
}
