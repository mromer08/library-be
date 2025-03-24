package com.ayd2.library.services.configuration;

import com.ayd2.library.dto.configurations.ConfigurationResponseDTO;
import com.ayd2.library.dto.configurations.UpdateConfigurationRequestDTO;
import com.ayd2.library.exceptions.NotFoundException;

public interface ConfigurationService {
    ConfigurationResponseDTO getConfiguration() throws NotFoundException;
    ConfigurationResponseDTO updateConfiguration(UpdateConfigurationRequestDTO updateRequest) throws NotFoundException;
}
