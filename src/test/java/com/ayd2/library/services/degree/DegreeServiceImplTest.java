package com.ayd2.library.services.degree;

import com.ayd2.library.dto.degrees.*;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.mappers.degree.DegreeMapper;
import com.ayd2.library.mappers.generic.GenericPageMapper;
import com.ayd2.library.models.degree.Degree;
import com.ayd2.library.repositories.degree.DegreeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DegreeServiceImplTest {

    private static final Long DEGREE_CODE = 101L;
    private static final String DEGREE_NAME = "Computer Science";
    private static final UUID DEGREE_ID = UUID.randomUUID();

    @Mock
    private DegreeRepository degreeRepository;

    @Mock
    private DegreeMapper degreeMapper;

    @Mock
    private GenericPageMapper degreePageMapper;

    @InjectMocks
    private DegreeServiceImpl serviceToTest;

    @Test
    void testCreateDegreeWhenCodeNotDuplicated() throws Exception {
        // Arrange
        NewDegreeRequestDTO requestDTO = new NewDegreeRequestDTO(DEGREE_CODE, DEGREE_NAME);
        Degree degree = new Degree();
        degree.setCode(DEGREE_CODE);
        degree.setName(DEGREE_NAME);

        Degree savedDegree = new Degree();
        savedDegree.setId(DEGREE_ID);
        savedDegree.setCode(DEGREE_CODE);
        savedDegree.setName(DEGREE_NAME);

        DegreeResponseDTO responseDTO = new DegreeResponseDTO(DEGREE_ID, DEGREE_CODE, DEGREE_NAME);

        ArgumentCaptor<Degree> captor = ArgumentCaptor.forClass(Degree.class);

        when(degreeRepository.existsByCode(DEGREE_CODE)).thenReturn(false);
        when(degreeMapper.toDegree(requestDTO)).thenReturn(degree);
        when(degreeRepository.save(captor.capture())).thenReturn(savedDegree);
        when(degreeMapper.toDegreeResponseDTO(savedDegree)).thenReturn(responseDTO);

        // Act
        DegreeResponseDTO result = serviceToTest.createDegree(requestDTO);

        // Assert
        assertAll(
                () -> assertEquals(DEGREE_CODE, captor.getValue().getCode()),
                () -> assertEquals(DEGREE_NAME, captor.getValue().getName()),
                () -> assertEquals(DEGREE_ID, result.id()),
                () -> assertEquals(DEGREE_CODE, result.code()),
                () -> assertEquals(DEGREE_NAME, result.name()));

        verify(degreeRepository, times(1)).existsByCode(DEGREE_CODE);
        verify(degreeMapper, times(1)).toDegree(requestDTO);
        verify(degreeRepository, times(1)).save(degree);
        verify(degreeMapper, times(1)).toDegreeResponseDTO(savedDegree);
    }

    @Test
    void testCreateDegreeWhenCodeDuplicated() {
        // Arrange
        NewDegreeRequestDTO requestDTO = new NewDegreeRequestDTO(DEGREE_CODE, DEGREE_NAME);

        when(degreeRepository.existsByCode(DEGREE_CODE)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntityException.class, () -> serviceToTest.createDegree(requestDTO));
        verify(degreeRepository, times(1)).existsByCode(DEGREE_CODE);
        verify(degreeRepository, never()).save(any(Degree.class));
    }

    @Test
    void testGetDegreeSuccessfully() throws Exception {
        // Arrange
        Degree degree = new Degree();
        degree.setId(DEGREE_ID);
        degree.setCode(DEGREE_CODE);
        degree.setName(DEGREE_NAME);

        DegreeResponseDTO responseDTO = new DegreeResponseDTO(DEGREE_ID, DEGREE_CODE, DEGREE_NAME);

        when(degreeRepository.findById(DEGREE_ID)).thenReturn(Optional.of(degree));
        when(degreeMapper.toDegreeResponseDTO(degree)).thenReturn(responseDTO);

        // Act
        DegreeResponseDTO result = serviceToTest.getDegree(DEGREE_ID);

        // Assert
        assertAll(
                () -> assertEquals(DEGREE_ID, result.id()),
                () -> assertEquals(DEGREE_CODE, result.code()),
                () -> assertEquals(DEGREE_NAME, result.name()));

        verify(degreeRepository, times(1)).findById(DEGREE_ID);
        verify(degreeMapper, times(1)).toDegreeResponseDTO(degree);
    }

    @Test
    void testGetDegreeWhenNotFound() {
        // Arrange
        when(degreeRepository.findById(DEGREE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> serviceToTest.getDegree(DEGREE_ID));

        verify(degreeRepository, times(1)).findById(DEGREE_ID);
        verify(degreeMapper, never()).toDegreeResponseDTO(any(Degree.class));
    }

    @Test
    void testUpdateDegreeSuccessfully() throws Exception {
        // Arrange
        UpdateDegreeRequestDTO requestDTO = new UpdateDegreeRequestDTO(DEGREE_CODE, DEGREE_NAME);
        Degree existingDegree = new Degree();
        existingDegree.setId(DEGREE_ID);
        existingDegree.setCode(100L);
        existingDegree.setName("Old Name");

        Degree updatedDegree = new Degree();
        updatedDegree.setId(DEGREE_ID);
        updatedDegree.setCode(DEGREE_CODE);
        updatedDegree.setName(DEGREE_NAME);

        DegreeResponseDTO responseDTO = new DegreeResponseDTO(DEGREE_ID, DEGREE_CODE, DEGREE_NAME);

        when(degreeRepository.findById(DEGREE_ID)).thenReturn(Optional.of(existingDegree));
        when(degreeRepository.existsByCodeAndIdNot(DEGREE_CODE, DEGREE_ID)).thenReturn(false);
        when(degreeRepository.save(existingDegree)).thenReturn(updatedDegree);
        when(degreeMapper.toDegreeResponseDTO(updatedDegree)).thenReturn(responseDTO);

        // Act
        DegreeResponseDTO result = serviceToTest.updateDegree(DEGREE_ID, requestDTO);

        // Assert
        assertAll(
                () -> assertEquals(DEGREE_ID, result.id()),
                () -> assertEquals(DEGREE_CODE, result.code()),
                () -> assertEquals(DEGREE_NAME, result.name()));

        verify(degreeRepository, times(1)).findById(DEGREE_ID);
        verify(degreeRepository, times(1)).existsByCodeAndIdNot(DEGREE_CODE, DEGREE_ID);
        verify(degreeMapper, times(1)).updateDegreeFromDTO(requestDTO, existingDegree);
        verify(degreeRepository, times(1)).save(existingDegree);
        verify(degreeMapper, times(1)).toDegreeResponseDTO(updatedDegree);
    }

    @Test
    void testUpdateDegreeWhenDegreeNotFound() {
        // Arrange
        UpdateDegreeRequestDTO requestDTO = new UpdateDegreeRequestDTO(DEGREE_CODE, DEGREE_NAME);

        when(degreeRepository.findById(DEGREE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> serviceToTest.updateDegree(DEGREE_ID, requestDTO));
        verify(degreeRepository, times(1)).findById(DEGREE_ID);
        verify(degreeRepository, never()).existsByCodeAndIdNot(anyLong(), any(UUID.class));
        verify(degreeRepository, never()).save(any(Degree.class));
    }

    @Test
    void testUpdateDegreeWhenCodeDuplicated() {
        // Arrange
        UpdateDegreeRequestDTO requestDTO = new UpdateDegreeRequestDTO(DEGREE_CODE, DEGREE_NAME);
        Degree existingDegree = new Degree();
        existingDegree.setId(DEGREE_ID);

        when(degreeRepository.findById(DEGREE_ID)).thenReturn(Optional.of(existingDegree));
        when(degreeRepository.existsByCodeAndIdNot(DEGREE_CODE, DEGREE_ID)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntityException.class, () -> serviceToTest.updateDegree(DEGREE_ID, requestDTO));
        verify(degreeRepository, times(1)).findById(DEGREE_ID);
        verify(degreeRepository, times(1)).existsByCodeAndIdNot(DEGREE_CODE, DEGREE_ID);
        verify(degreeRepository, never()).save(any(Degree.class));
    }

    @Test
    void testDeleteDegreeSuccessfully() throws Exception {
        // Arrange
        Degree degree = new Degree();
        degree.setId(DEGREE_ID);

        when(degreeRepository.findById(DEGREE_ID)).thenReturn(Optional.of(degree));

        // Act
        serviceToTest.deleteDegree(DEGREE_ID);

        // Assert
        verify(degreeRepository, times(1)).findById(DEGREE_ID);
        verify(degreeRepository, times(1)).delete(degree);
    }

    @Test
    void testDeleteDegreeWhenNotFound() {
        // Arrange
        when(degreeRepository.findById(DEGREE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> serviceToTest.deleteDegree(DEGREE_ID));

        verify(degreeRepository, times(1)).findById(DEGREE_ID);
        verify(degreeRepository, never()).delete(any(Degree.class));
    }

    @Test
    void testGetAllDegrees_ReturnsDegreeData() {
        // Arrange
        Degree degree = new Degree();
        degree.setId(DEGREE_ID);
        degree.setCode(DEGREE_CODE);
        degree.setName(DEGREE_NAME);

        List<Degree> degreeList = Collections.singletonList(degree);
        when(degreeRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(degreeList));

        DegreeResponseDTO expectedDto = new DegreeResponseDTO(DEGREE_ID, DEGREE_CODE, DEGREE_NAME);
        when(degreeMapper.toDegreeResponseDTO(degree)).thenReturn(expectedDto);

        // Configuración mínima del page mapper
        when(degreePageMapper.toPagedResponse(any())).thenReturn(
                new PagedResponseDTO<>(Collections.singletonList(expectedDto), 1, 0, 1, true, true, false, false));

        // Act
        PagedResponseDTO<DegreeResponseDTO> result = serviceToTest.getAllDegrees(Pageable.unpaged());

        // Assert - Verificamos los datos de los degrees
        assertNotNull(result);
        assertFalse(result.data().isEmpty());

        DegreeResponseDTO returnedDegree = result.data().get(0);
        assertAll(
                () -> assertEquals(DEGREE_ID, returnedDegree.id()),
                () -> assertEquals(DEGREE_CODE, returnedDegree.code()),
                () -> assertEquals(DEGREE_NAME, returnedDegree.name()));

        // Verificaciones básicas
        verify(degreeRepository).findAll(any(Pageable.class));
        verify(degreeMapper).toDegreeResponseDTO(degree);
    }
}