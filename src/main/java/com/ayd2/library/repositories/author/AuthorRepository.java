package com.ayd2.library.repositories.author;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd2.library.models.author.Author;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, UUID id);
}
