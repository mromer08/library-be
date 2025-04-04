package com.ayd2.library.controllers.reservation;

import com.ayd2.library.dto.reservation.NewReservationRequestDTO;
import com.ayd2.library.dto.reservation.ReservationResponseDTO;
import com.ayd2.library.dto.users.UserResponseDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.services.reservation.ReservationService;
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
@RequestMapping("/reservations")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserAccountService userAccountService;

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> createReservation(
            Authentication authentication,
            @RequestBody @Valid NewReservationRequestDTO reservationRequestDTO)
            throws DuplicatedEntityException, NotFoundException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserResponseDTO userAccount = userAccountService.getUserByEmail(userDetails.getUsername());
        ReservationResponseDTO response = reservationService.createReservation(userAccount.id(), reservationRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<PagedResponseDTO<ReservationResponseDTO>> getReservationsByAccount(
            Authentication authentication,
            @PageableDefault Pageable pageable) throws NotFoundException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserResponseDTO userAccount = userAccountService.getUserByEmail(userDetails.getUsername());
        PagedResponseDTO<ReservationResponseDTO> response = reservationService
                .getReservationsByAccount(userAccount.id(), pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservation(@PathVariable UUID id) throws NotFoundException {
        ReservationResponseDTO response = reservationService.getReservation(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id) throws NotFoundException {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}