package com.ayd2.library.repositories.degree;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd2.library.models.degree.Degree;

public interface DegreeRepository extends JpaRepository<Degree, UUID> {
    boolean existsByCode(Long code);
    boolean existsByCodeAndIdNot(Long code, UUID id);
}
