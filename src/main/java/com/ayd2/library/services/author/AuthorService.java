package com.ayd2.library.services.author;

import com.ayd2.library.dto.authors.*;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.exceptions.*;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

public interface AuthorService {
    AuthorResponseDTO createAuthor(NewAuthorRequestDTO authorRequestDTO) throws DuplicatedEntityException;
    AuthorResponseDTO updateAuthor(UUID id, UpdateAuthorRequestDTO authorRequestDTO) throws NotFoundException, DuplicatedEntityException;
    AuthorResponseDTO getAuthor(UUID id) throws NotFoundException;
    PagedResponseDTO<AuthorResponseDTO> getAllAuthors(Pageable pageable);
    void deleteAuthor(UUID id) throws NotFoundException;
}
