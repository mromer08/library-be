package com.ayd2.library.services.degree;

import com.ayd2.library.dto.degrees.DegreeResponseDTO;
import com.ayd2.library.dto.degrees.NewDegreeRequestDTO;
import com.ayd2.library.dto.degrees.UpdateDegreeRequestDTO;
import com.ayd2.library.dto.file.FileImportErrorDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.models.file.EntityType;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

public interface DegreeService {
    DegreeResponseDTO createDegree(NewDegreeRequestDTO degreeRequestDTO) throws DuplicatedEntityException;
    DegreeResponseDTO updateDegree(UUID id, UpdateDegreeRequestDTO degreeRequestDTO) throws NotFoundException, DuplicatedEntityException;
    DegreeResponseDTO getDegree(UUID id) throws NotFoundException;
    PagedResponseDTO<DegreeResponseDTO> getAllDegrees(Pageable pageable);
    void deleteDegree(UUID id) throws NotFoundException;
    boolean createFromText(String text, EntityType entityType, List<FileImportErrorDTO> errors) throws Exception;
}
