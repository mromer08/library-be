package com.ayd2.library.controllers.auth;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import com.ayd2.library.dto.auth.AuthResponseDTO;
import com.ayd2.library.dto.auth.LoginDTO;
import com.ayd2.library.dto.auth.StudentRegistrationRequestDTO;
import com.ayd2.library.exceptions.ServiceException;
import com.ayd2.library.services.auth.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginDTO loginDto)
            throws AuthenticationException, ServiceException {
        Map<String, String> tokens = authService.login(loginDto);
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.get("refreshToken"))
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofSeconds(60))
                .sameSite("None")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new AuthResponseDTO(tokens.get("accessToken")));

    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid StudentRegistrationRequestDTO registerDto)
            throws ServiceException {
        authService.register(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(@CookieValue String refreshToken) throws ServiceException {

        System.out.println("Refresh token: " + refreshToken);
        AuthResponseDTO authResponse = authService.refreshToken(refreshToken);
        System.out.println(authResponse);
        return ResponseEntity.ok(authResponse);
    }
}