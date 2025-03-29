package com.ayd2.library.repositories.student;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd2.library.models.student.Student;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    boolean existsByCarnet(Long carnet);
    boolean existsByCarnetAndIdNot(Long carnet, UUID id);
    Optional<Student> findByCarnet(Long carnet);
}
