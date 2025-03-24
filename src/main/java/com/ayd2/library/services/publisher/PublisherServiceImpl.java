package com.ayd2.library.services.publisher;

import com.ayd2.library.dto.publishers.PublisherRequestDTO;
import com.ayd2.library.dto.publishers.PublisherResponseDTO;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.mappers.publisher.PublisherMapper;
import com.ayd2.library.models.publisher.Publisher;
import com.ayd2.library.repositories.publisher.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper = PublisherMapper.INSTANCE;

    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Override
    public PublisherResponseDTO createPublisher(PublisherRequestDTO publisherRequestDTO) throws DuplicatedEntityException {
        if (publisherRepository.existsByName(publisherRequestDTO.name())) {
            throw new DuplicatedEntityException("A publisher with the name '" + publisherRequestDTO.name() + "' already exists.");
        }

        Publisher publisher = publisherMapper.toPublisher(publisherRequestDTO);
        Publisher savedPublisher = publisherRepository.save(publisher);
        return publisherMapper.toPublisherResponseDTO(savedPublisher);
    }

    @Override
    public PublisherResponseDTO updatePublisher(UUID id, PublisherRequestDTO publisherRequestDTO) throws NotFoundException, DuplicatedEntityException {
        Publisher existingPublisher = publisherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Publisher not found with id: " + id));

        if (publisherRepository.existsByNameAndIdNot(publisherRequestDTO.name(), id)) {
            throw new DuplicatedEntityException("A publisher with the name '" + publisherRequestDTO.name() + "' already exists.");
        }

        publisherMapper.updatePublisherFromDTO(publisherRequestDTO, existingPublisher);
        Publisher updatedPublisher = publisherRepository.save(existingPublisher);
        return publisherMapper.toPublisherResponseDTO(updatedPublisher);
    }

    @Override
    public PublisherResponseDTO getPublisher(UUID id) throws NotFoundException {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Publisher not found with id: " + id));
        return publisherMapper.toPublisherResponseDTO(publisher);
    }

    @Override
    public List<PublisherResponseDTO> getAllPublishers() {
        List<Publisher> publishers = publisherRepository.findAll();
        return publishers.stream()
                .map(publisherMapper::toPublisherResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePublisher(UUID id) throws NotFoundException {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Publisher not found with id: " + id));
        publisherRepository.delete(publisher);
    }
}