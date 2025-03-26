package com.ayd2.library.services.book;

import java.io.IOException;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.ayd2.library.dto.books.BookResponseDTO;
import com.ayd2.library.dto.books.NewBookRequestDTO;
import com.ayd2.library.dto.books.UpdateBookRequestDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;

import software.amazon.awssdk.core.exception.SdkException;

public interface BookService {
    BookResponseDTO createBook(NewBookRequestDTO bookRequestDTO) throws DuplicatedEntityException, NotFoundException, IOException, SdkException;
    BookResponseDTO updateBook(UUID id, UpdateBookRequestDTO bookRequestDTO) throws NotFoundException, IOException, SdkException;
    BookResponseDTO getBook(UUID id) throws NotFoundException;
    PagedResponseDTO<BookResponseDTO> getAllBooks(Pageable pageable);
    void deleteBook(UUID id) throws NotFoundException;

}
