package com.ayd2.library.dto.auth;

import com.ayd2.library.dto.students.NewStudentRequestDTO;
import com.ayd2.library.dto.users.NewUserAccountRequestDTO;

import jakarta.validation.Valid;

public record StudentRegistrationRequestDTO(
    @Valid NewUserAccountRequestDTO userAccount,
    @Valid NewStudentRequestDTO student
) {}
