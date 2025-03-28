package com.ayd2.library.services.author;

import com.ayd2.library.dto.authors.*;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.exceptions.*;
import com.ayd2.library.mappers.author.AuthorMapper;
import com.ayd2.library.mappers.generic.GenericPageMapper;
import com.ayd2.library.models.author.Author;
import com.ayd2.library.repositories.author.AuthorRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final GenericPageMapper authorPageMapper;

    @Override
    public AuthorResponseDTO createAuthor(NewAuthorRequestDTO authorRequestDTO) throws DuplicatedEntityException {
        if (authorRepository.existsByName(authorRequestDTO.name())) {
            throw new DuplicatedEntityException(
                    "An author with the name '" + authorRequestDTO.name() + "' already exists.");
        }

        Author author = authorMapper.toAuthor(authorRequestDTO);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toAuthorResponseDTO(savedAuthor);
    }

    @Override
    public AuthorResponseDTO updateAuthor(UUID id, UpdateAuthorRequestDTO authorRequestDTO)
            throws NotFoundException, DuplicatedEntityException {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found with id: " + id));

        if (authorRepository.existsByNameAndIdNot(authorRequestDTO.name(), id)) {
            throw new DuplicatedEntityException(
                    "An author with the name '" + authorRequestDTO.name() + "' already exists.");
        }

        authorMapper.updateAuthorFromDTO(authorRequestDTO, existingAuthor);
        Author updatedAuthor = authorRepository.save(existingAuthor);
        return authorMapper.toAuthorResponseDTO(updatedAuthor);
    }

    @Override
    public AuthorResponseDTO getAuthor(UUID id) throws NotFoundException {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found with id: " + id));
        return authorMapper.toAuthorResponseDTO(author);
    }

    @Override
    public PagedResponseDTO<AuthorResponseDTO> getAllAuthors(Pageable pageable) {
        Page<Author> page = authorRepository.findAll(pageable);
        Page<AuthorResponseDTO> dtoPage = page.map(authorMapper::toAuthorResponseDTO);
        return authorPageMapper.toPagedResponse(dtoPage);
    }

    @Override
    public void deleteAuthor(UUID id) throws NotFoundException {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found with id: " + id));
        authorRepository.delete(author);
    }
}
