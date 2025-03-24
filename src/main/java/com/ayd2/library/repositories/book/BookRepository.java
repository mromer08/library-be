package com.ayd2.library.repositories.book;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd2.library.models.book.Book;

public interface BookRepository extends JpaRepository<Book, UUID> {
    boolean existsByIsbn(String isbn);
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, UUID id);
    boolean existsByIsbnAndIdNot(String isbn, UUID id);
}
