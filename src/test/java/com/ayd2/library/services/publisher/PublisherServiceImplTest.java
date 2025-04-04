package com.ayd2.library.services.publisher;

import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.publishers.PublisherRequestDTO;
import com.ayd2.library.dto.publishers.PublisherResponseDTO;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.mappers.generic.GenericPageMapper;
import com.ayd2.library.mappers.publisher.PublisherMapper;
import com.ayd2.library.models.publisher.Publisher;
import com.ayd2.library.repositories.publisher.PublisherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublisherServiceImplTest {

    private static final String PUBLISHER_NAME = "Penguin Books";
    private static final UUID PUBLISHER_ID = UUID.randomUUID();

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private PublisherMapper publisherMapper;

    @Mock
    private GenericPageMapper publisherPageMapper;

    @InjectMocks
    private PublisherServiceImpl serviceToTest;

    @Test
    void testCreatePublisherWhenNameNotDuplicated() throws Exception {
        // Arrange
        PublisherRequestDTO requestDTO = new PublisherRequestDTO(PUBLISHER_NAME);
        Publisher publisher = new Publisher();
        publisher.setName(PUBLISHER_NAME);

        Publisher savedPublisher = new Publisher();
        savedPublisher.setId(PUBLISHER_ID);
        savedPublisher.setName(PUBLISHER_NAME);

        PublisherResponseDTO responseDTO = new PublisherResponseDTO(PUBLISHER_ID, PUBLISHER_NAME);

        ArgumentCaptor<Publisher> captor = ArgumentCaptor.forClass(Publisher.class);

        when(publisherRepository.existsByName(PUBLISHER_NAME)).thenReturn(false);
        when(publisherMapper.toPublisher(requestDTO)).thenReturn(publisher);
        when(publisherRepository.save(captor.capture())).thenReturn(savedPublisher);
        when(publisherMapper.toPublisherResponseDTO(savedPublisher)).thenReturn(responseDTO);

        // Act
        PublisherResponseDTO result = serviceToTest.createPublisher(requestDTO);

        // Assert
        assertAll(
                () -> assertEquals(PUBLISHER_NAME, captor.getValue().getName()),
                () -> assertEquals(PUBLISHER_ID, result.id()),
                () -> assertEquals(PUBLISHER_NAME, result.name()));

        verify(publisherRepository, times(1)).existsByName(PUBLISHER_NAME);
        verify(publisherMapper, times(1)).toPublisher(requestDTO);
        verify(publisherRepository, times(1)).save(publisher);
        verify(publisherMapper, times(1)).toPublisherResponseDTO(savedPublisher);
    }

    @Test
    void testCreatePublisherWhenNameDuplicated() {
        // Arrange
        PublisherRequestDTO requestDTO = new PublisherRequestDTO(PUBLISHER_NAME);

        when(publisherRepository.existsByName(PUBLISHER_NAME)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntityException.class, () -> serviceToTest.createPublisher(requestDTO));
        verify(publisherRepository, times(1)).existsByName(PUBLISHER_NAME);
        verify(publisherRepository, never()).save(any(Publisher.class));
    }

    @Test
    void testGetPublisherSuccessfully() throws Exception {
        // Arrange
        Publisher publisher = new Publisher();
        publisher.setId(PUBLISHER_ID);
        publisher.setName(PUBLISHER_NAME);

        PublisherResponseDTO responseDTO = new PublisherResponseDTO(PUBLISHER_ID, PUBLISHER_NAME);

        when(publisherRepository.findById(PUBLISHER_ID)).thenReturn(Optional.of(publisher));
        when(publisherMapper.toPublisherResponseDTO(publisher)).thenReturn(responseDTO);

        // Act
        PublisherResponseDTO result = serviceToTest.getPublisher(PUBLISHER_ID);

        // Assert
        assertAll(
                () -> assertEquals(PUBLISHER_ID, result.id()),
                () -> assertEquals(PUBLISHER_NAME, result.name()));

        verify(publisherRepository, times(1)).findById(PUBLISHER_ID);
        verify(publisherMapper, times(1)).toPublisherResponseDTO(publisher);
    }

    @Test
    void testGetPublisherWhenNotFound() {
        // Arrange
        when(publisherRepository.findById(PUBLISHER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> serviceToTest.getPublisher(PUBLISHER_ID));

        verify(publisherRepository, times(1)).findById(PUBLISHER_ID);
        verify(publisherMapper, never()).toPublisherResponseDTO(any(Publisher.class));
    }

    @Test
    void testUpdatePublisherSuccessfully() throws Exception {
        // Arrange
        PublisherRequestDTO requestDTO = new PublisherRequestDTO(PUBLISHER_NAME);
        Publisher existingPublisher = new Publisher();
        existingPublisher.setId(PUBLISHER_ID);
        existingPublisher.setName("Old Name");

        Publisher updatedPublisher = new Publisher();
        updatedPublisher.setId(PUBLISHER_ID);
        updatedPublisher.setName(PUBLISHER_NAME);

        PublisherResponseDTO responseDTO = new PublisherResponseDTO(PUBLISHER_ID, PUBLISHER_NAME);

        when(publisherRepository.findById(PUBLISHER_ID)).thenReturn(Optional.of(existingPublisher));
        when(publisherRepository.existsByNameAndIdNot(PUBLISHER_NAME, PUBLISHER_ID)).thenReturn(false);
        when(publisherRepository.save(existingPublisher)).thenReturn(updatedPublisher);
        when(publisherMapper.toPublisherResponseDTO(updatedPublisher)).thenReturn(responseDTO);

        // Act
        PublisherResponseDTO result = serviceToTest.updatePublisher(PUBLISHER_ID, requestDTO);

        // Assert
        assertAll(
                () -> assertEquals(PUBLISHER_ID, result.id()),
                () -> assertEquals(PUBLISHER_NAME, result.name()));

        verify(publisherRepository, times(1)).findById(PUBLISHER_ID);
        verify(publisherRepository, times(1)).existsByNameAndIdNot(PUBLISHER_NAME, PUBLISHER_ID);
        verify(publisherMapper, times(1)).updatePublisherFromDTO(requestDTO, existingPublisher);
        verify(publisherRepository, times(1)).save(existingPublisher);
        verify(publisherMapper, times(1)).toPublisherResponseDTO(updatedPublisher);
    }

    @Test
    void testUpdatePublisherWhenPublisherNotFound() {
        // Arrange
        PublisherRequestDTO requestDTO = new PublisherRequestDTO(PUBLISHER_NAME);

        when(publisherRepository.findById(PUBLISHER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> serviceToTest.updatePublisher(PUBLISHER_ID, requestDTO));
        verify(publisherRepository, times(1)).findById(PUBLISHER_ID);
        verify(publisherRepository, never()).existsByNameAndIdNot(anyString(), any(UUID.class));
        verify(publisherRepository, never()).save(any(Publisher.class));
    }

    @Test
    void testUpdatePublisherWhenNameDuplicated() {
        // Arrange
        PublisherRequestDTO requestDTO = new PublisherRequestDTO(PUBLISHER_NAME);
        Publisher existingPublisher = new Publisher();
        existingPublisher.setId(PUBLISHER_ID);

        when(publisherRepository.findById(PUBLISHER_ID)).thenReturn(Optional.of(existingPublisher));
        when(publisherRepository.existsByNameAndIdNot(PUBLISHER_NAME, PUBLISHER_ID)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntityException.class, () -> serviceToTest.updatePublisher(PUBLISHER_ID, requestDTO));
        verify(publisherRepository, times(1)).findById(PUBLISHER_ID);
        verify(publisherRepository, times(1)).existsByNameAndIdNot(PUBLISHER_NAME, PUBLISHER_ID);
        verify(publisherRepository, never()).save(any(Publisher.class));
    }

    @Test
    void testDeletePublisherSuccessfully() throws Exception {
        // Arrange
        Publisher publisher = new Publisher();
        publisher.setId(PUBLISHER_ID);

        when(publisherRepository.findById(PUBLISHER_ID)).thenReturn(Optional.of(publisher));

        // Act
        serviceToTest.deletePublisher(PUBLISHER_ID);

        // Assert
        verify(publisherRepository, times(1)).findById(PUBLISHER_ID);
        verify(publisherRepository, times(1)).delete(publisher);
    }

    @Test
    void testDeletePublisherWhenNotFound() {
        // Arrange
        when(publisherRepository.findById(PUBLISHER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> serviceToTest.deletePublisher(PUBLISHER_ID));

        verify(publisherRepository, times(1)).findById(PUBLISHER_ID);
        verify(publisherRepository, never()).delete(any(Publisher.class));
    }

    @Test
    void testGetAllPublishers_ReturnsPublisherData() {
        // Arrange
        Publisher publisher = new Publisher();
        publisher.setId(PUBLISHER_ID);
        publisher.setName(PUBLISHER_NAME);

        List<Publisher> publisherList = Collections.singletonList(publisher);
        when(publisherRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(publisherList));

        PublisherResponseDTO expectedDto = new PublisherResponseDTO(PUBLISHER_ID, PUBLISHER_NAME);
        when(publisherMapper.toPublisherResponseDTO(publisher)).thenReturn(expectedDto);

        when(publisherPageMapper.toPagedResponse(any())).thenReturn(
                new PagedResponseDTO<>(Collections.singletonList(expectedDto), 1, 0, 1, true, true, false, false));

        // Act
        PagedResponseDTO<PublisherResponseDTO> result = serviceToTest.getAllPublishers(Pageable.unpaged());

        PublisherResponseDTO returnedPublisher = result.data().get(0);
        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.data().isEmpty()),
                () -> assertEquals(PUBLISHER_ID, returnedPublisher.id()),
                () -> assertEquals(PUBLISHER_NAME, returnedPublisher.name()));

        verify(publisherRepository).findAll(any(Pageable.class));
        verify(publisherMapper).toPublisherResponseDTO(publisher);
    }
}
