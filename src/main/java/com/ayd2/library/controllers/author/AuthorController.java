package com.ayd2.library.controllers.author;

import com.ayd2.library.dto.authors.*;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.exceptions.ServiceException;
import com.ayd2.library.services.author.AuthorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<AuthorResponseDTO> createAuthor(@RequestBody @Valid NewAuthorRequestDTO authorRequestDTO)
            throws ServiceException {
        AuthorResponseDTO responseDTO = authorService.createAuthor(authorRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> updateAuthor(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateAuthorRequestDTO authorRequestDTO) throws ServiceException {
        AuthorResponseDTO responseDTO = authorService.updateAuthor(id, authorRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> getAuthor(@PathVariable UUID id) throws ServiceException {
        AuthorResponseDTO responseDTO = authorService.getAuthor(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<PagedResponseDTO<AuthorResponseDTO>> getAllAuthors(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(authorService.getAllAuthors(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable UUID id) throws ServiceException {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
