package com.ayd2.library.services.publisher;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.publishers.PublisherRequestDTO;
import com.ayd2.library.dto.publishers.PublisherResponseDTO;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;

public interface PublisherService {
    PublisherResponseDTO createPublisher(PublisherRequestDTO publisherRequestDTO) throws NotFoundException, DuplicatedEntityException;
    PublisherResponseDTO updatePublisher(UUID id, PublisherRequestDTO publisherRequestDTO) throws NotFoundException, DuplicatedEntityException;
    PublisherResponseDTO getPublisher(UUID id) throws NotFoundException;
    PagedResponseDTO<PublisherResponseDTO> getAllPublishers(Pageable pageable);
    void deletePublisher(UUID id) throws NotFoundException;
}
