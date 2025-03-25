package com.ayd2.library.controllers.book;

import com.ayd2.library.dto.books.BookResponseDTO;
import com.ayd2.library.dto.books.NewBookRequestDTO;
import com.ayd2.library.dto.books.UpdateBookRequestDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.exceptions.ServiceException;
import com.ayd2.library.services.book.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@RequestBody @Valid NewBookRequestDTO bookRequestDTO)
            throws ServiceException {
        BookResponseDTO responseDTO = bookService.createBook(bookRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateBookRequestDTO bookRequestDTO) throws ServiceException {
        BookResponseDTO responseDTO = bookService.updateBook(id, bookRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBook(@PathVariable UUID id) throws ServiceException {
        BookResponseDTO responseDTO = bookService.getBook(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<PagedResponseDTO<BookResponseDTO>> getAllBooks(
            @PageableDefault Pageable pageable) {

        PagedResponseDTO<BookResponseDTO> response = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) throws ServiceException {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
