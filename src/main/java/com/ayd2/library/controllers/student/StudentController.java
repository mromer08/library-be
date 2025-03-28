package com.ayd2.library.controllers.student;

import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.students.StudentDetailResponseDTO;
import com.ayd2.library.dto.students.StudentResponseDTO;
import com.ayd2.library.exceptions.ServiceException;
import com.ayd2.library.services.student.StudentService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // @PostMapping
    // public ResponseEntity<StudentDetailResponseDTO> createStudent(@RequestBody @Valid Student newStudent)
    //         throws Exception {
    //     Student createdStudent = studentService.createStudent(newStudent);
    //     return ResponseEntity.status(HttpStatus.CREATED)
    //             .body(studentService.getStudentById(createdStudent.getId()));
    // }

    // @PutMapping("/{id}")
    // public ResponseEntity<StudentDetailResponseDTO> updateStudent(
    //         @PathVariable UUID id, 
    //         @RequestBody @Valid Student updateStudent) throws Exception {
    //     Student updatedStudent = studentService.updateStudent(id, updateStudent);
    //     return ResponseEntity.ok(studentService.getStudentById(updatedStudent.getId()));
    // }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDetailResponseDTO> getStudentById(@PathVariable UUID id) throws ServiceException {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping
    public ResponseEntity<PagedResponseDTO<StudentResponseDTO>> getAllStudents(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(studentService.getAllStudents(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID id) throws ServiceException {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}

