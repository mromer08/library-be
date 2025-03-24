package com.ayd2.library.controllers.publisher;

import com.ayd2.library.dto.publishers.PublisherRequestDTO;
import com.ayd2.library.dto.publishers.PublisherResponseDTO;
import com.ayd2.library.exceptions.ServiceException;
import com.ayd2.library.services.publisher.PublisherService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    @PostMapping
    public ResponseEntity<PublisherResponseDTO> createPublisher(@RequestBody PublisherRequestDTO publisherRequestDTO)
            throws ServiceException {
        PublisherResponseDTO responseDTO = publisherService.createPublisher(publisherRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherResponseDTO> updatePublisher(
            @PathVariable UUID id,
            @RequestBody PublisherRequestDTO publisherRequestDTO) throws ServiceException {
        PublisherResponseDTO responseDTO = publisherService.updatePublisher(id, publisherRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponseDTO> getPublisher(@PathVariable UUID id) throws ServiceException {
        PublisherResponseDTO responseDTO = publisherService.getPublisher(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<PublisherResponseDTO>> getAllPublishers() {
        List<PublisherResponseDTO> publishers = publisherService.getAllPublishers();
        return ResponseEntity.ok(publishers);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable UUID id) throws ServiceException {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
}