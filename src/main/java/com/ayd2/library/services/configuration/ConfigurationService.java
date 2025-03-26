package com.ayd2.library.services.configuration;

import java.io.IOException;

import com.ayd2.library.dto.configurations.ConfigurationResponseDTO;
import com.ayd2.library.dto.configurations.UpdateConfigurationRequestDTO;
import com.ayd2.library.exceptions.NotFoundException;

import software.amazon.awssdk.core.exception.SdkException;

public interface ConfigurationService {
    ConfigurationResponseDTO getConfiguration() throws NotFoundException;
    ConfigurationResponseDTO updateConfiguration(UpdateConfigurationRequestDTO updateRequest) throws NotFoundException, IOException, SdkException;
}
