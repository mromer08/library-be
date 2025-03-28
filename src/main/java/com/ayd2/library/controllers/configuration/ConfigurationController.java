package com.ayd2.library.controllers.configuration;

import com.ayd2.library.dto.configurations.ConfigurationResponseDTO;
import com.ayd2.library.dto.configurations.UpdateConfigurationRequestDTO;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.services.configuration.ConfigurationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.exception.SdkException;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configuration")
@RequiredArgsConstructor
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @GetMapping
    public ResponseEntity<ConfigurationResponseDTO> getConfiguration() throws NotFoundException {
        ConfigurationResponseDTO response = configurationService.getConfiguration();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ConfigurationResponseDTO> updateConfiguration(
            @ModelAttribute @Valid UpdateConfigurationRequestDTO updateRequest) throws NotFoundException, IOException, SdkException {
        ConfigurationResponseDTO response = configurationService.updateConfiguration(updateRequest);
        return ResponseEntity.ok(response);
    }
}
