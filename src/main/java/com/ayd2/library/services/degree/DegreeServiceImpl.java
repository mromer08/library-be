package com.ayd2.library.services.degree;

import com.ayd2.library.dto.degrees.*;
import com.ayd2.library.exceptions.*;
import com.ayd2.library.mappers.degree.DegreeMapper;
import com.ayd2.library.models.degree.Degree;
import com.ayd2.library.repositories.degree.DegreeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DegreeServiceImpl implements DegreeService {

    private final DegreeRepository degreeRepository;
    private final DegreeMapper degreeMapper = DegreeMapper.INSTANCE;

    public DegreeServiceImpl(DegreeRepository degreeRepository) {
        this.degreeRepository = degreeRepository;
    }

    @Override
    public DegreeResponseDTO createDegree(NewDegreeRequestDTO degreeRequestDTO) throws DuplicatedEntityException {
        if (degreeRepository.existsByCode(degreeRequestDTO.code())) {
            throw new DuplicatedEntityException("A degree with the code '" + degreeRequestDTO.code() + "' already exists.");
        }

        Degree degree = degreeMapper.toDegree(degreeRequestDTO);
        Degree savedDegree = degreeRepository.save(degree);
        return degreeMapper.toDegreeResponseDTO(savedDegree);
    }

    @Override
    public DegreeResponseDTO updateDegree(UUID id, UpdateDegreeRequestDTO degreeRequestDTO) throws NotFoundException, DuplicatedEntityException {
        Degree existingDegree = degreeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Degree not found with id: " + id));

        if (degreeRepository.existsByCodeAndIdNot(degreeRequestDTO.code(), id)) {
            throw new DuplicatedEntityException("A degree with the code '" + degreeRequestDTO.code() + "' already exists.");
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
    public List<DegreeResponseDTO> getAllDegrees() {
        List<Degree> degrees = degreeRepository.findAll();
        return degrees.stream()
                .map(degreeMapper::toDegreeResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDegree(UUID id) throws NotFoundException {
        Degree degree = degreeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Degree not found with id: " + id));
        degreeRepository.delete(degree);
    }
}
