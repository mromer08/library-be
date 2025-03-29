package com.ayd2.library.mappers.auth;

import com.ayd2.library.dto.students.NewStudentRequestDTO;
import com.ayd2.library.dto.users.NewUserAccountRequestDTO;
import com.ayd2.library.models.student.Student;
import com.ayd2.library.models.user.UserAccount;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StudentRegistrationMapper {

    StudentRegistrationMapper INSTANCE = Mappers.getMapper(StudentRegistrationMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "isApproved", constant = "false")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "imageUrl", ignore = true)
    UserAccount toUserAccount(NewUserAccountRequestDTO userAccountDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userAccount", ignore = true)
    @Mapping(target = "degree", ignore = true)
    @Mapping(target = "isSanctioned", ignore = true)
    Student toStudent(NewStudentRequestDTO studentDTO);
}
