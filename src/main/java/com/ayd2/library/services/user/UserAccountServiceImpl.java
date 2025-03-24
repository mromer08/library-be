package com.ayd2.library.services.user;

import com.ayd2.library.dto.users.UpdateUserAccountRequestDTO;
import com.ayd2.library.dto.users.UserResponseDTO;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.mappers.user.UserMapper;
import com.ayd2.library.models.user.UserAccount;
import com.ayd2.library.repositories.user.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(UUID id, UpdateUserAccountRequestDTO dto) throws NotFoundException, DuplicatedEntityException {
        UserAccount userAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        if (userAccountRepository.existsByEmailAndIdNot(dto.email(), id)) {
            throw new DuplicatedEntityException("A user with the email '" + dto.email() + "' already exists.");
        }

        userMapper.updateUserFromDTO(dto, userAccount);
        UserAccount updatedUser = userAccountRepository.save(userAccount);
        return userMapper.toUserResponseDTO(updatedUser);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) throws NotFoundException {
        UserAccount userAccount = userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
        return userMapper.toUserResponseDTO(userAccount);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userAccountRepository.findAll().stream()
                .map(userMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) throws NotFoundException {
        UserAccount userAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        userAccountRepository.delete(userAccount);
    }
}

