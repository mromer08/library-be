package com.ayd2.library.controllers.loan;

import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.loan.*;
import com.ayd2.library.exceptions.*;
import com.ayd2.library.services.loan.LoanService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(@RequestBody @Valid NewLoanRequestDTO loanRequestDTO)
            throws ServiceException {
        LoanResponseDTO responseDTO = loanService.createLoan(loanRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> getLoanById(@PathVariable UUID id) throws NotFoundException {
        LoanResponseDTO responseDTO = loanService.getLoanById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<PagedResponseDTO<LoanResponseDTO>> getAllLoans(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(loanService.getAllLoans(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable UUID id) throws NotFoundException {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

}
