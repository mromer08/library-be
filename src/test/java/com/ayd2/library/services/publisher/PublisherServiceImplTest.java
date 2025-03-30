package com.ayd2.library.services.publisher;

import com.ayd2.library.dto.publishers.PublisherRequestDTO;
import com.ayd2.library.dto.publishers.PublisherResponseDTO;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.models.publisher.Publisher;
import com.ayd2.library.repositories.publisher.PublisherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublisherServiceImplTest {

    private static final String PUBLISHER_NAME = "Penguin Books";
    private static final UUID PUBLISHER_ID = UUID.randomUUID();

    @Mock
    PublisherRepository publisherRepository;
    
    @InjectMocks
    PublisherServiceImpl serviceToTest;

    @Test
    void testCreatePublisherWhenNameNotDuplicated() throws Exception {
        // Arrange
        PublisherRequestDTO dto = new PublisherRequestDTO(PUBLISHER_NAME);
        Publisher publisher = new Publisher();
        publisher.setName(PUBLISHER_NAME);

        Publisher savedPublisher = new Publisher();
        savedPublisher.setId(PUBLISHER_ID);

        ArgumentCaptor<Publisher> captor = ArgumentCaptor.forClass(Publisher.class);

        when(publisherRepository.existsByName(PUBLISHER_NAME))
                .thenReturn(false);
        when(publisherRepository.save(captor.capture()))
                .thenReturn(savedPublisher);

        // Act
        PublisherResponseDTO result = serviceToTest.createPublisher(dto);

        // Assert
        assertAll(
                () -> assertEquals(PUBLISHER_NAME, captor.getValue().getName()),
                () -> assertNotNull(result)
        );
    }

    @Test
    void testCreatePublisherWhenNameDuplicated() {
        // Arrange
        PublisherRequestDTO dto = new PublisherRequestDTO(PUBLISHER_NAME);

        when(publisherRepository.existsByName(PUBLISHER_NAME))
                .thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntityException.class, () -> serviceToTest.createPublisher(dto));
        verify(publisherRepository, never()).save(any(Publisher.class));
    }

    @Test
    void testUpdatePublisherWhenPublisherExistsAndNameNotDuplicated() throws Exception {
        // Arrange
        PublisherRequestDTO dto = new PublisherRequestDTO(PUBLISHER_NAME);
        Publisher existingPublisher = new Publisher();
        existingPublisher.setId(PUBLISHER_ID);

        Publisher updatedPublisher = new Publisher();
        updatedPublisher.setId(PUBLISHER_ID);

        ArgumentCaptor<Publisher> captor = ArgumentCaptor.forClass(Publisher.class);

        when(publisherRepository.findById(PUBLISHER_ID))
                .thenReturn(Optional.of(existingPublisher));
        when(publisherRepository.existsByNameAndIdNot(PUBLISHER_NAME, PUBLISHER_ID))
                .thenReturn(false);
        when(publisherRepository.save(captor.capture()))
                .thenReturn(updatedPublisher);

        // Act
        PublisherResponseDTO result = serviceToTest.updatePublisher(PUBLISHER_ID, dto);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> verify(publisherRepository).findById(PUBLISHER_ID),
                () -> verify(publisherRepository).existsByNameAndIdNot(PUBLISHER_NAME, PUBLISHER_ID)
        );
    }

    @Test
    void testUpdatePublisherWhenPublisherNotFound() {
        // Arrange
        PublisherRequestDTO dto = new PublisherRequestDTO(PUBLISHER_NAME);

        when(publisherRepository.findById(PUBLISHER_ID))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> serviceToTest.updatePublisher(PUBLISHER_ID, dto));
        verify(publisherRepository, never()).save(any(Publisher.class));
    }

    @Test
    void testUpdatePublisherWhenNameDuplicated() {
        // Arrange
        PublisherRequestDTO dto = new PublisherRequestDTO(PUBLISHER_NAME);
        Publisher existingPublisher = new Publisher();
        existingPublisher.setId(PUBLISHER_ID);

        when(publisherRepository.findById(PUBLISHER_ID))
                .thenReturn(Optional.of(existingPublisher));
        when(publisherRepository.existsByNameAndIdNot(PUBLISHER_NAME, PUBLISHER_ID))
                .thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntityException.class, () -> serviceToTest.updatePublisher(PUBLISHER_ID, dto));
        verify(publisherRepository, never()).save(any(Publisher.class));
    }

    @Test
    void testGetPublisherWhenPublisherExists() throws Exception {
        // Arrange
        Publisher publisher = new Publisher();
        publisher.setId(PUBLISHER_ID);

        when(publisherRepository.findById(PUBLISHER_ID))
                .thenReturn(Optional.of(publisher));

        // Act
        PublisherResponseDTO result = serviceToTest.getPublisher(PUBLISHER_ID);

        // Assert
        assertNotNull(result);
        verify(publisherRepository).findById(PUBLISHER_ID);
    }

    @Test
    void testGetPublisherWhenPublisherNotFound() {
        // Arrange
        when(publisherRepository.findById(PUBLISHER_ID))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> serviceToTest.getPublisher(PUBLISHER_ID));
    }

    @Test
    void testDeletePublisherWhenPublisherExists() throws Exception {
        // Arrange
        Publisher publisher = new Publisher();
        publisher.setId(PUBLISHER_ID);

        when(publisherRepository.findById(PUBLISHER_ID))
                .thenReturn(Optional.of(publisher));

        // Act
        serviceToTest.deletePublisher(PUBLISHER_ID);

        // Assert
        verify(publisherRepository).delete(publisher);
    }

    @Test
    void testDeletePublisherWhenPublisherNotFound() {
        // Arrange
        when(publisherRepository.findById(PUBLISHER_ID))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> serviceToTest.deletePublisher(PUBLISHER_ID));
        verify(publisherRepository, never()).delete(any(Publisher.class));
    }
}