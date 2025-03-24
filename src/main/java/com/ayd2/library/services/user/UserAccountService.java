package com.ayd2.library.services.user;

import java.util.List;
import java.util.UUID;

import com.ayd2.library.dto.users.UpdateUserAccountRequestDTO;
import com.ayd2.library.dto.users.UserResponseDTO;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;

public interface UserAccountService {
    public UserResponseDTO updateUser(UUID id, UpdateUserAccountRequestDTO dto) throws NotFoundException, DuplicatedEntityException;
    UserResponseDTO getUserByEmail(String email) throws NotFoundException;
    List<UserResponseDTO> getAllUsers();
    void deleteUser(UUID id) throws NotFoundException;
}
