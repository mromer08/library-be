package com.ayd2.library.services.configuration;

import com.ayd2.library.dto.configurations.ConfigurationResponseDTO;
import com.ayd2.library.dto.configurations.UpdateConfigurationRequestDTO;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.mappers.configuration.ConfigurationMapper;
import com.ayd2.library.models.configuration.Configuration;
import com.ayd2.library.repositories.configuration.ConfigurationRepository;

import org.springframework.stereotype.Service;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final ConfigurationMapper configurationMapper = ConfigurationMapper.INSTANCE;

    public ConfigurationServiceImpl(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    public ConfigurationResponseDTO getConfiguration() throws NotFoundException {
        Configuration configuration = configurationRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException("Configuration not found"));
        return configurationMapper.toConfigurationResponseDTO(configuration);
    }

    public ConfigurationResponseDTO updateConfiguration(UpdateConfigurationRequestDTO updateRequest)
            throws NotFoundException {
        Configuration configuration = configurationRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException("Configuration not found"));

        configurationMapper.updateConfigurationFromDTO(updateRequest, configuration);
        Configuration updatedConfiguration = configurationRepository.save(configuration);

        return configurationMapper.toConfigurationResponseDTO(updatedConfiguration);
    }
}
