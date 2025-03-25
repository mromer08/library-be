package com.ayd2.library.services.book;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.ayd2.library.dto.books.BookResponseDTO;
import com.ayd2.library.dto.books.NewBookRequestDTO;
import com.ayd2.library.dto.books.UpdateBookRequestDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;

public interface BookService {
    BookResponseDTO createBook(NewBookRequestDTO bookRequestDTO) throws DuplicatedEntityException, NotFoundException;
    BookResponseDTO updateBook(UUID id, UpdateBookRequestDTO bookRequestDTO) throws NotFoundException;
    BookResponseDTO getBook(UUID id) throws NotFoundException;
    PagedResponseDTO<BookResponseDTO> getAllBooks(Pageable pageable);
    void deleteBook(UUID id) throws NotFoundException;

}
