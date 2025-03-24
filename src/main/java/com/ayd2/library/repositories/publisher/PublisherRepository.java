package com.ayd2.library.repositories.publisher;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd2.library.models.publisher.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, UUID> {
    Optional<Publisher> findByName(String name);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, UUID id);
    
}
