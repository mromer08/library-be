package com.ayd2.library.repositories.book;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd2.library.models.book.Book;

public interface BookRepository extends JpaRepository<Book, UUID> {
    boolean existsByIsbn(String isbn);

    boolean existsByCode(String code);

    // @Query("SELECT b FROM book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%',
    // :query, '%')) " +
    // "OR LOWER(b.code) LIKE LOWER(CONCAT('%', :query, '%')) " +
    // "OR LOWER(b.isbn) LIKE LOWER(CONCAT('%', :query, '%'))")
    // Page<Book> search(@Param("query") String query, Pageable pageable);

}
