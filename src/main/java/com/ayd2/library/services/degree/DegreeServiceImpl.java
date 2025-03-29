package com.ayd2.library.services.degree;

import com.ayd2.library.dto.degrees.*;
import com.ayd2.library.dto.file.FileImportErrorDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.exceptions.*;
import com.ayd2.library.mappers.degree.DegreeMapper;
import com.ayd2.library.mappers.generic.GenericPageMapper;
import com.ayd2.library.models.degree.Degree;
import com.ayd2.library.models.file.EntityType;
import com.ayd2.library.repositories.degree.DegreeRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class DegreeServiceImpl implements DegreeService {

    private final DegreeRepository degreeRepository;
    private final DegreeMapper degreeMapper;
    private final GenericPageMapper degreePageMapper;

    @Override
    public DegreeResponseDTO createDegree(NewDegreeRequestDTO degreeRequestDTO) throws DuplicatedEntityException {
        if (degreeRepository.existsByCode(degreeRequestDTO.code())) {
            throw new DuplicatedEntityException(
                    "A degree with the code '" + degreeRequestDTO.code() + "' already exists.");
        }

        Degree degree = degreeMapper.toDegree(degreeRequestDTO);
        Degree savedDegree = degreeRepository.save(degree);
        return degreeMapper.toDegreeResponseDTO(savedDegree);
    }

    @Override
    public DegreeResponseDTO updateDegree(UUID id, UpdateDegreeRequestDTO degreeRequestDTO)
            throws NotFoundException, DuplicatedEntityException {
        Degree existingDegree = degreeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Degree not found with id: " + id));

        if (degreeRepository.existsByCodeAndIdNot(degreeRequestDTO.code(), id)) {
            throw new DuplicatedEntityException(
                    "A degree with the code '" + degreeRequestDTO.code() + "' already exists.");
        }

        degreeMapper.updateDegreeFromDTO(degreeRequestDTO, existingDegree);
        Degree updatedDegree = degreeRepository.save(existingDegree);
        return degreeMapper.toDegreeResponseDTO(updatedDegree);
    }

    @Override
    public DegreeResponseDTO getDegree(UUID id) throws NotFoundException {
        Degree degree = degreeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Degree not found with id: " + id));
        return degreeMapper.toDegreeResponseDTO(degree);
    }

    @Override
    public PagedResponseDTO<DegreeResponseDTO> getAllDegrees(Pageable pageable) {
        Page<Degree> page = degreeRepository.findAll(pageable);
        Page<DegreeResponseDTO> dtoPage = page.map(degreeMapper::toDegreeResponseDTO);
        return degreePageMapper.toPagedResponse(dtoPage);
    }

    @Override
    public void deleteDegree(UUID id) throws NotFoundException {
        Degree degree = degreeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Degree not found with id: " + id));
        degreeRepository.delete(degree);
    }

    @Override
    public boolean createFromText(String text, EntityType entityType, List<FileImportErrorDTO> errors)
            throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createFromText'");
    }
}
