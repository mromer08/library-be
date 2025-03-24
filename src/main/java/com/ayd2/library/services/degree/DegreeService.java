package com.ayd2.library.services.degree;

import com.ayd2.library.dto.degrees.DegreeResponseDTO;
import com.ayd2.library.dto.degrees.NewDegreeRequestDTO;
import com.ayd2.library.dto.degrees.UpdateDegreeRequestDTO;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface DegreeService {
    DegreeResponseDTO createDegree(NewDegreeRequestDTO degreeRequestDTO) throws DuplicatedEntityException;
    DegreeResponseDTO updateDegree(UUID id, UpdateDegreeRequestDTO degreeRequestDTO) throws NotFoundException, DuplicatedEntityException;
    DegreeResponseDTO getDegree(UUID id) throws NotFoundException;
    List<DegreeResponseDTO> getAllDegrees();
    void deleteDegree(UUID id) throws NotFoundException;
}
