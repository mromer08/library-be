package com.ayd2.library.repositories.student;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd2.library.models.student.Student;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    
}
