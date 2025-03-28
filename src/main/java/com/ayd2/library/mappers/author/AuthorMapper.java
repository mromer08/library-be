package com.ayd2.library.mappers.author;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.ayd2.library.dto.authors.*;
import com.ayd2.library.models.author.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorResponseDTO toAuthorResponseDTO(Author author);

    @Mapping(target = "id", ignore = true)
    Author toAuthor(NewAuthorRequestDTO authorRequestDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateAuthorFromDTO(UpdateAuthorRequestDTO dto, @MappingTarget Author author);
}
