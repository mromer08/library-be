package com.ayd2.library.services.auth;

import com.ayd2.library.dto.auth.AuthResponseDTO;
import com.ayd2.library.dto.auth.LoginDTO;
import com.ayd2.library.dto.auth.StudentRegistrationRequestDTO;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.exceptions.ServiceException;
import com.ayd2.library.mappers.auth.StudentRegistrationMapper;
import com.ayd2.library.models.degree.Degree;
import com.ayd2.library.models.student.Student;
import com.ayd2.library.models.user.Role;
import com.ayd2.library.models.user.UserAccount;
import com.ayd2.library.repositories.degree.DegreeRepository;
import com.ayd2.library.repositories.student.StudentRepository;
import com.ayd2.library.repositories.user.RoleRepository;
import com.ayd2.library.repositories.user.UserAccountRepository;
import com.ayd2.library.security.JwtTokenService;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AuthServiceImpl implements AuthService {

    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final StudentRepository studentRepository;
    private final DegreeRepository degreeRepository;
    private final StudentRegistrationMapper studentRegistrationMapper;

    public Map<String, String> login(LoginDTO loginDto) throws AuthenticationException, ServiceException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.email(),
                        loginDto.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenService.generateAccessToken(authentication.getName());
        String refreshToken = jwtTokenService.generateRefreshToken(authentication.getName());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    @Transactional
    public boolean register(StudentRegistrationRequestDTO request) throws ServiceException {
        if (userAccountRepository.existsByEmail(request.userAccount().email())) {
            throw new DuplicatedEntityException("Email already in use");
        }

        if (userAccountRepository.existsByCui(request.userAccount().cui())) {
            throw new DuplicatedEntityException("CUI already in use");
        }

        if (studentRepository.existsByCarnet(request.student().carnet())) {
            throw new DuplicatedEntityException("Academic record number already in use");
        }

        if (!roleRepository.existsByName("STUDENT")) {
            throw new NotFoundException("Role STUDENT not found");
        }

        if (!degreeRepository.existsById(request.student().degreeId())) {
            throw new NotFoundException("Degree not found");
        }

        UserAccount userAccount = studentRegistrationMapper.toUserAccount(request.userAccount());
        Student student = studentRegistrationMapper.toStudent(request.student());

        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));

        Role studentRole = roleRepository.findByName("STUDENT").get();
        userAccount.setRole(studentRole);

        Degree studentDegree = degreeRepository.findById(request.student().degreeId()).get();

        UserAccount savedUserAccount = userAccountRepository.save(userAccount);
        student.setUserAccount(savedUserAccount);
        student.setDegree(studentDegree);
        studentRepository.save(student);

        return true;
    }

    @Override
    public AuthResponseDTO refreshToken(String refreshToken) throws ServiceException {
        if (!jwtTokenService.validateRefreshToken(refreshToken)) {
            throw new ServiceException("Invalid refresh token");
        }
        String username = jwtTokenService.getUsernameFromRefreshToken(refreshToken);
        String newAccessToken = jwtTokenService.generateAccessToken(username);
        return new AuthResponseDTO(newAccessToken);
    }
}