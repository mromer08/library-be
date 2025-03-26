package com.ayd2.library.services.user;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.ayd2.library.dto.users.UpdateUserAccountRequestDTO;
import com.ayd2.library.dto.users.UserResponseDTO;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;

import software.amazon.awssdk.core.exception.SdkException;

public interface UserAccountService {
    UserResponseDTO updateUser(UUID id, UpdateUserAccountRequestDTO dto) throws NotFoundException, DuplicatedEntityException, IOException, SdkException;
    UserResponseDTO getUserByEmail(String email) throws NotFoundException;
    List<UserResponseDTO> getAllUsers();
    void deleteUser(UUID id) throws NotFoundException;
}
