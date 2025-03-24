package com.ayd2.library.repositories.configuration;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd2.library.models.configuration.Configuration;

public interface ConfigurationRepository extends JpaRepository<Configuration, UUID> {
    Optional<Configuration> findFirstByOrderByIdAsc();
}
