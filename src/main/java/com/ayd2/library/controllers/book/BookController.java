package com.ayd2.library.controllers.book;

import com.ayd2.library.dto.books.BookResponseDTO;
import com.ayd2.library.dto.books.BookResponseDetailDTO;
import com.ayd2.library.dto.books.BookSearchRequestDTO;
import com.ayd2.library.dto.books.NewBookRequestDTO;
import com.ayd2.library.dto.books.UpdateBookRequestDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.exceptions.ServiceException;
import com.ayd2.library.services.book.BookService;

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
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<BookResponseDTO> createBook(@ModelAttribute @Valid NewBookRequestDTO bookRequestDTO)
            throws Exception {
        BookResponseDTO responseDTO = bookService.createBook(bookRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable UUID id,
            @ModelAttribute @Valid UpdateBookRequestDTO bookRequestDTO) throws Exception {
        BookResponseDTO responseDTO = bookService.updateBook(id, bookRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDetailDTO> getBook(@PathVariable UUID id) throws ServiceException {
        BookResponseDetailDTO responseDTO = bookService.getBook(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<PagedResponseDTO<BookResponseDTO>> getAllBooks(
            @Valid BookSearchRequestDTO request, @PageableDefault Pageable pageable) {

        PagedResponseDTO<BookResponseDTO> response = bookService.getAllBooks(request, pageable);
        return ResponseEntity.ok(response);
    }

    // @GetMapping("/search")
    // public ResponseEntity<PagedResponseDTO<BookResponseDTO>> searchBooks(
    //         @Valid BookSearchRequestDTO request, @PageableDefault Pageable pageable) {
    //     return ResponseEntity.ok(bookService.searchBooks(request, pageable));
    // }


    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) throws ServiceException {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
