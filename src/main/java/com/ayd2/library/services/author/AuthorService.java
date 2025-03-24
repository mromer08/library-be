package com.ayd2.library.services.author;

import com.ayd2.library.dto.authors.*;
import com.ayd2.library.exceptions.*;

import java.util.List;
import java.util.UUID;

public interface AuthorService {
    AuthorResponseDTO createAuthor(NewAuthorRequestDTO authorRequestDTO) throws DuplicatedEntityException;
    AuthorResponseDTO updateAuthor(UUID id, UpdateAuthorRequestDTO authorRequestDTO) throws NotFoundException, DuplicatedEntityException;
    AuthorResponseDTO getAuthor(UUID id) throws NotFoundException;
    List<AuthorResponseDTO> getAllAuthors();
    void deleteAuthor(UUID id) throws NotFoundException;
}
