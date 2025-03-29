package com.ayd2.library.services.student;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.students.StudentDetailResponseDTO;
import com.ayd2.library.dto.students.StudentResponseDTO;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.mappers.generic.GenericPageMapper;
import com.ayd2.library.mappers.student.StudentMapper;
import com.ayd2.library.models.degree.Degree;
import com.ayd2.library.models.student.Student;
import com.ayd2.library.repositories.degree.DegreeRepository;
import com.ayd2.library.repositories.student.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final DegreeRepository degreeRepository;
    private final StudentMapper studentMapper;
    private final GenericPageMapper studentPageMapper;

    @Override
    public Student createStudent(Student newStudent) throws DuplicatedEntityException, NotFoundException {
        if (studentRepository.existsByCarnet(newStudent.getCarnet())) {
            throw new DuplicatedEntityException("Academic record number already in use");
        }

        Degree degree = degreeRepository.findById(newStudent.getDegree().getId())
                .orElseThrow(() -> new NotFoundException("Degree not found"));

        newStudent.setDegree(degree);
        return studentRepository.save(newStudent);
    }

    @Override
    public Student updateStudent(UUID id, Student updateStudent) throws DuplicatedEntityException, NotFoundException {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        if (studentRepository.existsByCarnetAndIdNot(updateStudent.getCarnet(), id)) {
            throw new DuplicatedEntityException("Academic record number already in use");
        }

        existingStudent.setCarnet(updateStudent.getCarnet());
        // existingStudent.setPenalty(updateStudent.getPenalty());

        Degree degree = degreeRepository.findById(updateStudent.getDegree().getId())
                .orElseThrow(() -> new NotFoundException("Degree not found"));
        existingStudent.setDegree(degree);

        return studentRepository.save(existingStudent);
    }

    @Override
    public StudentDetailResponseDTO getStudentById(UUID id) throws NotFoundException {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + id));
        return studentMapper.toStudentDetailResponseDTO(student);
    }

    @Override
    public PagedResponseDTO<StudentResponseDTO> getAllStudents(Pageable pageable) {
        Page<Student> page = studentRepository.findAll(pageable);
        Page<StudentResponseDTO> dtoPage = page.map(studentMapper::toStudentResponseDTO);
        return studentPageMapper.toPagedResponse(dtoPage);
    }

    @Override
    public void deleteStudent(UUID id) throws NotFoundException {
        if (!studentRepository.existsById(id)) {
            throw new NotFoundException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

}
