package com.ayd2.library.dto.users;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;

public record UpdateUserAccountRequestDTO(
    @Email(message = "Email is not valid")    
    String email,

    MultipartFile imageFile
) {}
