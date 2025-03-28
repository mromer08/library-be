package com.ayd2.library.controllers.user;

import com.ayd2.library.dto.users.UserResponseDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.users.UpdateUserAccountRequestDTO;
import com.ayd2.library.dto.users.UserDetailResponseDTO;
import com.ayd2.library.exceptions.ServiceException;
import com.ayd2.library.services.user.UserAccountService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserAccountController {

    private final UserAccountService userAccountService;

    @GetMapping
    public ResponseEntity<PagedResponseDTO<UserResponseDTO>> getAllUsers(@PageableDefault Pageable pageable) {
        PagedResponseDTO<UserResponseDTO> users = userAccountService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getAuthenticatedUser(Authentication authentication) throws Exception {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(userAccountService.getUserByEmail(userDetails.getUsername()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateAuthenticatedUser(
            Authentication authentication,
            @ModelAttribute @Valid UpdateUserAccountRequestDTO dto) throws Exception {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UUID userId = userAccountService.getUserByEmail(userDetails.getUsername()).id();
        UserResponseDTO responseDTO = userAccountService.updateUser(userId, dto);
        return ResponseEntity.ok(responseDTO);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserAccountRequestDTO dto) throws Exception {
        UserResponseDTO responseDTO = userAccountService.updateUser(id, dto);
        return ResponseEntity.ok(responseDTO);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) throws ServiceException {
        userAccountService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponseDTO> getUserById(@PathVariable UUID id) throws ServiceException {
        UserDetailResponseDTO responseDTO = userAccountService.getUserById(id);
        return ResponseEntity.ok(responseDTO);
    }
}
