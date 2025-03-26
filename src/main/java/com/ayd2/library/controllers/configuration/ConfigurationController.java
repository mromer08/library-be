package com.ayd2.library.controllers.configuration;

import com.ayd2.library.dto.configurations.ConfigurationResponseDTO;
import com.ayd2.library.dto.configurations.UpdateConfigurationRequestDTO;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.services.configuration.ConfigurationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
    public ResponseEntity<ConfigurationResponseDTO> updateConfiguration(
            @ModelAttribute @Valid UpdateConfigurationRequestDTO updateRequest) throws NotFoundException {
        ConfigurationResponseDTO response = configurationService.updateConfiguration(updateRequest);
        return ResponseEntity.ok(response);
    }
}
