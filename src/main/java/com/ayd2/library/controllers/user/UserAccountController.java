package com.ayd2.library.controllers.user;

import com.ayd2.library.dto.users.UserResponseDTO;
import com.ayd2.library.dto.users.UpdateUserAccountRequestDTO;
import com.ayd2.library.exceptions.ServiceException;
import com.ayd2.library.services.user.UserAccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userAccountService.getAllUsers();
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
            @RequestBody @Valid UpdateUserAccountRequestDTO dto) throws Exception {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UUID userId = userAccountService.getUserByEmail(userDetails.getUsername()).id();
        UserResponseDTO responseDTO = userAccountService.updateUser(userId, dto);
        return ResponseEntity.ok(responseDTO);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserAccountRequestDTO dto) throws ServiceException {
        UserResponseDTO responseDTO = userAccountService.updateUser(id, dto);
        return ResponseEntity.ok(responseDTO);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) throws ServiceException {
        userAccountService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // @GetMapping("/{email}")
    // public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) throws ServiceException {
    //     UserResponseDTO responseDTO = userAccountService.getUserByEmail(email);
    //     return ResponseEntity.ok(responseDTO);
    // }
}
