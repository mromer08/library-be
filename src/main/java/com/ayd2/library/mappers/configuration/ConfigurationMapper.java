package com.ayd2.library.mappers.configuration;

import com.ayd2.library.dto.configurations.ConfigurationResponseDTO;
import com.ayd2.library.dto.configurations.UpdateConfigurationRequestDTO;
import com.ayd2.library.models.configuration.Configuration;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ConfigurationMapper {

    ConfigurationMapper INSTANCE = Mappers.getMapper(ConfigurationMapper.class);

    ConfigurationResponseDTO toConfigurationResponseDTO(Configuration configuration);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "logo", ignore = true)
    void updateConfigurationFromDTO(UpdateConfigurationRequestDTO dto, @MappingTarget Configuration configuration);
}
