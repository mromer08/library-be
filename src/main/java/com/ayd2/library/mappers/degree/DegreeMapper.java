package com.ayd2.library.mappers.degree;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.ayd2.library.dto.degrees.DegreeResponseDTO;
import com.ayd2.library.dto.degrees.NewDegreeRequestDTO;
import com.ayd2.library.dto.degrees.UpdateDegreeRequestDTO;
import com.ayd2.library.models.degree.Degree;

@Mapper
public interface DegreeMapper {
    DegreeMapper INSTANCE = Mappers.getMapper(DegreeMapper.class);

    DegreeResponseDTO toDegreeResponseDTO(Degree degree);

    @Mapping(target = "id", ignore = true)
    Degree toDegree(NewDegreeRequestDTO degreeRequestDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateDegreeFromDTO(UpdateDegreeRequestDTO dto, @MappingTarget Degree degree);
}
