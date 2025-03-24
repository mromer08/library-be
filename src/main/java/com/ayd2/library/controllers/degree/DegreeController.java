package com.ayd2.library.controllers.degree;

import com.ayd2.library.dto.degrees.DegreeResponseDTO;
import com.ayd2.library.dto.degrees.NewDegreeRequestDTO;
import com.ayd2.library.dto.degrees.UpdateDegreeRequestDTO;
import com.ayd2.library.exceptions.ServiceException;
import com.ayd2.library.services.degree.DegreeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/degrees")
@RequiredArgsConstructor
public class DegreeController {

    private final DegreeService degreeService;

    @PostMapping
    public ResponseEntity<DegreeResponseDTO> createDegree(@RequestBody @Valid NewDegreeRequestDTO degreeRequestDTO)
            throws ServiceException {
        DegreeResponseDTO responseDTO = degreeService.createDegree(degreeRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
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
    public ResponseEntity<List<DegreeResponseDTO>> getAllDegrees() {
        List<DegreeResponseDTO> degrees = degreeService.getAllDegrees();
        return ResponseEntity.ok(degrees);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDegree(@PathVariable UUID id) throws ServiceException {
        degreeService.deleteDegree(id);
        return ResponseEntity.noContent().build();
    }
}
