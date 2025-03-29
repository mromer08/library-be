package com.ayd2.library.services.student;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.ayd2.library.dto.file.FileImportErrorDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.students.StudentDetailResponseDTO;
import com.ayd2.library.dto.students.StudentResponseDTO;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.models.file.EntityType;
import com.ayd2.library.models.student.Student;

public interface StudentService {
    Student createStudent(Student newStudent) throws DuplicatedEntityException, NotFoundException;
    Student updateStudent(UUID id, Student student) throws DuplicatedEntityException, NotFoundException;
    StudentDetailResponseDTO getStudentById(UUID id) throws NotFoundException;
    PagedResponseDTO<StudentResponseDTO> getAllStudents(Pageable pageable);
    void deleteStudent(UUID id) throws NotFoundException;
    Boolean createFromText(String text, EntityType entityType, List<FileImportErrorDTO> errors) throws Exception;
}
