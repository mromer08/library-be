package com.ayd2.library.services.user;

import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.users.UpdateUserAccountRequestDTO;
import com.ayd2.library.dto.users.UserDetailResponseDTO;
import com.ayd2.library.dto.users.UserResponseDTO;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.mappers.generic.GenericPageMapper;
import com.ayd2.library.mappers.user.UserMapper;
import com.ayd2.library.models.user.UserAccount;
import com.ayd2.library.repositories.user.UserAccountRepository;
import com.ayd2.library.services.s3.S3Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.exception.SdkException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final S3Service s3Service;
    private final UserMapper userMapper;
    private final GenericPageMapper pageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponseDTO updateUser(UUID id, UpdateUserAccountRequestDTO dto)
            throws NotFoundException, DuplicatedEntityException, IOException, SdkException {
        UserAccount userAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        if (userAccountRepository.existsByEmailAndIdNot(dto.email(), id)) {
            throw new DuplicatedEntityException("A user with the email '" + dto.email() + "' already exists.");
        }

        MultipartFile imageFile = dto.imageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String imageUrl = s3Service.uploadFile(uuid.toString(), imageFile);
            userAccount.setImageUrl(imageUrl);
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
    public UserDetailResponseDTO getUserById(UUID id) throws NotFoundException {
        UserAccount userAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return userMapper.toUserDetailResponseDTO(userAccount);
    }

    @Override
    public PagedResponseDTO<UserResponseDTO> getAllUsers(Pageable pageable) {
        Page<UserAccount> page = userAccountRepository.findAll(pageable);
        Page<UserResponseDTO> dtoPage = page.map(userMapper::toUserResponseDTO);
        return pageMapper.toPagedResponse(dtoPage);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) throws NotFoundException {
        UserAccount userAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        userAccountRepository.delete(userAccount);
    }
}
