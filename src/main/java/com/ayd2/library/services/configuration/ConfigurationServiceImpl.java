package com.ayd2.library.services.configuration;

import com.ayd2.library.dto.configurations.ConfigurationResponseDTO;
import com.ayd2.library.dto.configurations.UpdateConfigurationRequestDTO;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.mappers.configuration.ConfigurationMapper;
import com.ayd2.library.models.configuration.Configuration;
import com.ayd2.library.repositories.configuration.ConfigurationRepository;
import com.ayd2.library.services.s3.S3Service;

import jakarta.transaction.Transactional;
import software.amazon.awssdk.core.exception.SdkException;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(rollbackOn = Exception.class)
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final S3Service s3Service;
    private final ConfigurationMapper configurationMapper = ConfigurationMapper.INSTANCE;

    public ConfigurationServiceImpl(ConfigurationRepository configurationRepository, S3Service s3Service) {
        this.s3Service = s3Service;
        this.configurationRepository = configurationRepository;
    }

    public ConfigurationResponseDTO getConfiguration() throws NotFoundException {
        Configuration configuration = configurationRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException("Configuration not found"));
        return configurationMapper.toConfigurationResponseDTO(configuration);
    }

    public ConfigurationResponseDTO updateConfiguration(UpdateConfigurationRequestDTO updateRequest)
            throws NotFoundException, IOException, SdkException {
        Configuration configuration = configurationRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new NotFoundException("Configuration not found"));

        MultipartFile imageFile = updateRequest.imageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String imageUrl = s3Service.uploadFile(uuid.toString(), imageFile);
            configuration.setLogo(imageUrl);
        }

        configurationMapper.updateConfigurationFromDTO(updateRequest, configuration);
        Configuration updatedConfiguration = configurationRepository.save(configuration);

        return configurationMapper.toConfigurationResponseDTO(updatedConfiguration);
    }
}
