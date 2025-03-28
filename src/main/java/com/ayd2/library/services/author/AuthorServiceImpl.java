package com.ayd2.library.services.author;

import com.ayd2.library.dto.authors.*;
import com.ayd2.library.exceptions.*;
import com.ayd2.library.mappers.author.AuthorMapper;
import com.ayd2.library.models.author.Author;
import com.ayd2.library.repositories.author.AuthorRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

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
    public List<AuthorResponseDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(authorMapper::toAuthorResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAuthor(UUID id) throws NotFoundException {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found with id: " + id));
        authorRepository.delete(author);
    }
}
