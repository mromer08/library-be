package com.ayd2.library.mappers.publisher;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.ayd2.library.dto.publishers.PublisherRequestDTO;
import com.ayd2.library.dto.publishers.PublisherResponseDTO;
import com.ayd2.library.models.publisher.Publisher;

@Mapper
public interface PublisherMapper {
    PublisherMapper INSTANCE = Mappers.getMapper(PublisherMapper.class);

    PublisherResponseDTO toPublisherResponseDTO(Publisher publisher);
    
    @Mapping(target = "id", ignore = true)
    Publisher toPublisher(PublisherRequestDTO publisherRequestDTO);
    
    @Mapping(target = "id", ignore = true)
    void updatePublisherFromDTO(PublisherRequestDTO dto, @MappingTarget Publisher publisher);
}
