package com.ayd2.library.services.auth;

import java.util.Map;

import org.springframework.security.core.AuthenticationException;

import com.ayd2.library.dto.auth.AuthResponseDTO;
import com.ayd2.library.dto.auth.LoginDTO;
import com.ayd2.library.dto.auth.StudentRegistrationRequestDTO;
import com.ayd2.library.exceptions.ServiceException;

public interface AuthService {
    public boolean register(StudentRegistrationRequestDTO request) throws ServiceException;
    public Map<String, String> login(LoginDTO loginDto) throws AuthenticationException, ServiceException;
    public AuthResponseDTO refreshToken(String refreshToken) throws ServiceException;
}
