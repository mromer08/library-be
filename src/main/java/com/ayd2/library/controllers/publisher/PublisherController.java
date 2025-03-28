package com.ayd2.library.controllers.publisher;

import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.publishers.PublisherRequestDTO;
import com.ayd2.library.dto.publishers.PublisherResponseDTO;
import com.ayd2.library.exceptions.ServiceException;
import com.ayd2.library.services.publisher.PublisherService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<PublisherResponseDTO> createPublisher(@RequestBody @Valid PublisherRequestDTO publisherRequestDTO)
            throws ServiceException {
        PublisherResponseDTO responseDTO = publisherService.createPublisher(publisherRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<PublisherResponseDTO> updatePublisher(
            @PathVariable UUID id,
            @RequestBody @Valid PublisherRequestDTO publisherRequestDTO) throws ServiceException {
        PublisherResponseDTO responseDTO = publisherService.updatePublisher(id, publisherRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponseDTO> getPublisher(@PathVariable UUID id) throws ServiceException {
        PublisherResponseDTO responseDTO = publisherService.getPublisher(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<PagedResponseDTO<PublisherResponseDTO>> getAllPublishers(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(publisherService.getAllPublishers(pageable));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> deletePublisher(@PathVariable UUID id) throws ServiceException {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
}