package com.ayd2.library.controllers.degree;

import com.ayd2.library.dto.degrees.DegreeResponseDTO;
import com.ayd2.library.dto.degrees.NewDegreeRequestDTO;
import com.ayd2.library.dto.degrees.UpdateDegreeRequestDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.exceptions.ServiceException;
import com.ayd2.library.services.degree.DegreeService;

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
@RequestMapping("/degrees")
@RequiredArgsConstructor
public class DegreeController {

    private final DegreeService degreeService;

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<DegreeResponseDTO> createDegree(@RequestBody @Valid NewDegreeRequestDTO degreeRequestDTO)
            throws ServiceException {
        DegreeResponseDTO responseDTO = degreeService.createDegree(degreeRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<DegreeResponseDTO> updateDegree(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateDegreeRequestDTO degreeRequestDTO) throws ServiceException {
        DegreeResponseDTO responseDTO = degreeService.updateDegree(id, degreeRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DegreeResponseDTO> getDegree(@PathVariable UUID id) throws ServiceException {
        DegreeResponseDTO responseDTO = degreeService.getDegree(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<PagedResponseDTO<DegreeResponseDTO>> getAllDegrees(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(degreeService.getAllDegrees(pageable));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> deleteDegree(@PathVariable UUID id) throws ServiceException {
        degreeService.deleteDegree(id);
        return ResponseEntity.noContent().build();
    }
}
