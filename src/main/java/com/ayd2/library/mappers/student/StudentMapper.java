package com.ayd2.library.mappers.student;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.ayd2.library.dto.students.StudentDetailResponseDTO;
import com.ayd2.library.dto.students.StudentResponseDTO;
import com.ayd2.library.models.student.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(source = "userAccount.id", target = "user.id")
    @Mapping(source = "userAccount.name", target = "user.name")
    @Mapping(source = "userAccount.email", target = "user.email")
    StudentDetailResponseDTO toStudentDetailResponseDTO(Student student);

    @Mapping(source = "userAccount.imageUrl", target = "imageUrl")
    @Mapping(source = "userAccount.name", target = "name")
    StudentResponseDTO toStudentResponseDTO(Student student);

}

