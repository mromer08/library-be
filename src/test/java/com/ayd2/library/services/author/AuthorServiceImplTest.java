package com.ayd2.library.services.author;

import com.ayd2.library.dto.authors.*;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.exceptions.*;
import com.ayd2.library.mappers.author.AuthorMapper;
import com.ayd2.library.mappers.generic.GenericPageMapper;
import com.ayd2.library.models.author.Author;
import com.ayd2.library.repositories.author.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    private static final String AUTHOR_NAME = "J.K. Rowling";
    private static final String AUTHOR_NATIONALITY = "British";
    private static final LocalDate AUTHOR_BIRTH_DATE = LocalDate.of(1965, 7, 31);
    private static final UUID AUTHOR_ID = UUID.randomUUID();

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private GenericPageMapper authorPageMapper;

    @InjectMocks
    private AuthorServiceImpl serviceToTest;

    @Test
    void testCreateAuthorWhenNameNotDuplicated() throws Exception {
        // Arrange
        NewAuthorRequestDTO requestDTO = new NewAuthorRequestDTO(AUTHOR_NAME, AUTHOR_NATIONALITY, AUTHOR_BIRTH_DATE);
        Author author = new Author();
        author.setName(AUTHOR_NAME);
        author.setNationality(AUTHOR_NATIONALITY);
        author.setBirthDate(AUTHOR_BIRTH_DATE);

        Author savedAuthor = new Author();
        savedAuthor.setId(AUTHOR_ID);
        savedAuthor.setName(AUTHOR_NAME);
        savedAuthor.setNationality(AUTHOR_NATIONALITY);
        savedAuthor.setBirthDate(AUTHOR_BIRTH_DATE);

        AuthorResponseDTO responseDTO = new AuthorResponseDTO(AUTHOR_ID, AUTHOR_NAME, AUTHOR_NATIONALITY,
                AUTHOR_BIRTH_DATE);

        ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);

        when(authorRepository.existsByName(AUTHOR_NAME)).thenReturn(false);
        when(authorMapper.toAuthor(requestDTO)).thenReturn(author);
        when(authorRepository.save(captor.capture())).thenReturn(savedAuthor);
        when(authorMapper.toAuthorResponseDTO(savedAuthor)).thenReturn(responseDTO);

        // Act
        AuthorResponseDTO result = serviceToTest.createAuthor(requestDTO);

        // Assert
        assertAll(
                () -> assertEquals(AUTHOR_NAME, captor.getValue().getName()),
                () -> assertEquals(AUTHOR_NATIONALITY, captor.getValue().getNationality()),
                () -> assertEquals(AUTHOR_BIRTH_DATE, captor.getValue().getBirthDate()),
                () -> assertEquals(AUTHOR_ID, result.id()),
                () -> assertEquals(AUTHOR_NAME, result.name()),
                () -> assertEquals(AUTHOR_NATIONALITY, result.nationality()),
                () -> assertEquals(AUTHOR_BIRTH_DATE, result.birthDate()));

        verify(authorRepository, times(1)).existsByName(AUTHOR_NAME);
        verify(authorMapper, times(1)).toAuthor(requestDTO);
        verify(authorRepository, times(1)).save(author);
        verify(authorMapper, times(1)).toAuthorResponseDTO(savedAuthor);
    }

    @Test
    void testCreateAuthorWhenNameDuplicated() {
        // Arrange
        NewAuthorRequestDTO requestDTO = new NewAuthorRequestDTO(AUTHOR_NAME, AUTHOR_NATIONALITY, AUTHOR_BIRTH_DATE);

        when(authorRepository.existsByName(AUTHOR_NAME)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntityException.class, () -> serviceToTest.createAuthor(requestDTO));
        verify(authorRepository, times(1)).existsByName(AUTHOR_NAME);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void testGetAuthorSuccessfully() throws Exception {
        // Arrange
        Author author = new Author();
        author.setId(AUTHOR_ID);
        author.setName(AUTHOR_NAME);
        author.setNationality(AUTHOR_NATIONALITY);
        author.setBirthDate(AUTHOR_BIRTH_DATE);

        AuthorResponseDTO responseDTO = new AuthorResponseDTO(AUTHOR_ID, AUTHOR_NAME, AUTHOR_NATIONALITY,
                AUTHOR_BIRTH_DATE);

        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
        when(authorMapper.toAuthorResponseDTO(author)).thenReturn(responseDTO);

        // Act
        AuthorResponseDTO result = serviceToTest.getAuthor(AUTHOR_ID);

        // Assert
        assertAll(
                () -> assertEquals(AUTHOR_ID, result.id()),
                () -> assertEquals(AUTHOR_NAME, result.name()),
                () -> assertEquals(AUTHOR_NATIONALITY, result.nationality()),
                () -> assertEquals(AUTHOR_BIRTH_DATE, result.birthDate()));

        verify(authorRepository, times(1)).findById(AUTHOR_ID);
        verify(authorMapper, times(1)).toAuthorResponseDTO(author);
    }

    @Test
    void testGetAuthorWhenNotFound() {
        // Arrange
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> serviceToTest.getAuthor(AUTHOR_ID));

        verify(authorRepository, times(1)).findById(AUTHOR_ID);
        verify(authorMapper, never()).toAuthorResponseDTO(any(Author.class));
    }

    @Test
    void testUpdateAuthorSuccessfully() throws Exception {
        // Arrange
        UpdateAuthorRequestDTO requestDTO = new UpdateAuthorRequestDTO(AUTHOR_NAME, AUTHOR_NATIONALITY,
                AUTHOR_BIRTH_DATE);
        Author existingAuthor = new Author();
        existingAuthor.setId(AUTHOR_ID);
        existingAuthor.setName("Old Name");
        existingAuthor.setNationality("Old Nationality");
        existingAuthor.setBirthDate(LocalDate.of(1900, 1, 1));

        Author updatedAuthor = new Author();
        updatedAuthor.setId(AUTHOR_ID);
        updatedAuthor.setName(AUTHOR_NAME);
        updatedAuthor.setNationality(AUTHOR_NATIONALITY);
        updatedAuthor.setBirthDate(AUTHOR_BIRTH_DATE);

        AuthorResponseDTO responseDTO = new AuthorResponseDTO(AUTHOR_ID, AUTHOR_NAME, AUTHOR_NATIONALITY,
                AUTHOR_BIRTH_DATE);

        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.existsByNameAndIdNot(AUTHOR_NAME, AUTHOR_ID)).thenReturn(false);
        when(authorRepository.save(existingAuthor)).thenReturn(updatedAuthor);
        when(authorMapper.toAuthorResponseDTO(updatedAuthor)).thenReturn(responseDTO);

        // Act
        AuthorResponseDTO result = serviceToTest.updateAuthor(AUTHOR_ID, requestDTO);

        // Assert
        assertAll(
                () -> assertEquals(AUTHOR_ID, result.id()),
                () -> assertEquals(AUTHOR_NAME, result.name()),
                () -> assertEquals(AUTHOR_NATIONALITY, result.nationality()),
                () -> assertEquals(AUTHOR_BIRTH_DATE, result.birthDate()));

        verify(authorRepository, times(1)).findById(AUTHOR_ID);
        verify(authorRepository, times(1)).existsByNameAndIdNot(AUTHOR_NAME, AUTHOR_ID);
        verify(authorMapper, times(1)).updateAuthorFromDTO(requestDTO, existingAuthor);
        verify(authorRepository, times(1)).save(existingAuthor);
        verify(authorMapper, times(1)).toAuthorResponseDTO(updatedAuthor);
    }

    @Test
    void testUpdateAuthorWhenAuthorNotFound() {
        // Arrange
        UpdateAuthorRequestDTO requestDTO = new UpdateAuthorRequestDTO(AUTHOR_NAME, AUTHOR_NATIONALITY,
                AUTHOR_BIRTH_DATE);

        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> serviceToTest.updateAuthor(AUTHOR_ID, requestDTO));
        verify(authorRepository, times(1)).findById(AUTHOR_ID);
        verify(authorRepository, never()).existsByNameAndIdNot(anyString(), any(UUID.class));
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void testUpdateAuthorWhenNameDuplicated() {
        // Arrange
        UpdateAuthorRequestDTO requestDTO = new UpdateAuthorRequestDTO(AUTHOR_NAME, AUTHOR_NATIONALITY,
                AUTHOR_BIRTH_DATE);
        Author existingAuthor = new Author();
        existingAuthor.setId(AUTHOR_ID);

        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.existsByNameAndIdNot(AUTHOR_NAME, AUTHOR_ID)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntityException.class, () -> serviceToTest.updateAuthor(AUTHOR_ID, requestDTO));
        verify(authorRepository, times(1)).findById(AUTHOR_ID);
        verify(authorRepository, times(1)).existsByNameAndIdNot(AUTHOR_NAME, AUTHOR_ID);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void testDeleteAuthorSuccessfully() throws Exception {
        // Arrange
        Author author = new Author();
        author.setId(AUTHOR_ID);

        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));

        // Act
        serviceToTest.deleteAuthor(AUTHOR_ID);

        // Assert
        verify(authorRepository, times(1)).findById(AUTHOR_ID);
        verify(authorRepository, times(1)).delete(author);
    }

    @Test
    void testDeleteAuthorWhenNotFound() {
        // Arrange
        when(authorRepository.findById(AUTHOR_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> serviceToTest.deleteAuthor(AUTHOR_ID));

        verify(authorRepository, times(1)).findById(AUTHOR_ID);
        verify(authorRepository, never()).delete(any(Author.class));
    }

    @Test
    void testGetAllAuthors_ReturnsAuthorData() {
        // Arrange
        Author author = new Author();
        author.setId(AUTHOR_ID);
        author.setName(AUTHOR_NAME);
        author.setNationality(AUTHOR_NATIONALITY);
        author.setBirthDate(AUTHOR_BIRTH_DATE);

        List<Author> authorList = Collections.singletonList(author);
        when(authorRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(authorList));

        AuthorResponseDTO expectedDto = new AuthorResponseDTO(
                AUTHOR_ID, AUTHOR_NAME, AUTHOR_NATIONALITY, AUTHOR_BIRTH_DATE);
        when(authorMapper.toAuthorResponseDTO(author)).thenReturn(expectedDto);

        // Configuración mínima del page mapper
        when(authorPageMapper.toPagedResponse(any())).thenReturn(
                new PagedResponseDTO<>(Collections.singletonList(expectedDto), 1, 0, 1, true, true, false, false));

        // Act
        PagedResponseDTO<AuthorResponseDTO> result = serviceToTest.getAllAuthors(Pageable.unpaged());

        // Assert - Solo verificamos los datos de los autores
        assertNotNull(result);
        assertFalse(result.data().isEmpty());

        AuthorResponseDTO returnedAuthor = result.data().get(0);
        assertAll(
                () -> assertEquals(AUTHOR_ID, returnedAuthor.id()),
                () -> assertEquals(AUTHOR_NAME, returnedAuthor.name()),
                () -> assertEquals(AUTHOR_NATIONALITY, returnedAuthor.nationality()),
                () -> assertEquals(AUTHOR_BIRTH_DATE, returnedAuthor.birthDate()));

        // Verificaciones básicas
        verify(authorRepository).findAll(any(Pageable.class));
        verify(authorMapper).toAuthorResponseDTO(author);
    }
}
