package com.ayd2.library.mappers.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.dto.users.UserResponseDTO;

@Mapper
public interface UserPageMapper {

    UserPageMapper INSTANCE = Mappers.getMapper(UserPageMapper.class);

    @Mapping(target = "data", source = "content")
    @Mapping(target = "pageNumber", source = "number")
    @Mapping(target = "isFirst", expression = "java(page.isFirst())")
    @Mapping(target = "isLast", expression = "java(page.isLast())")
    @Mapping(target = "hasNext", expression = "java(page.hasNext())")
    @Mapping(target = "hasPrevious", expression = "java(page.hasPrevious())")
    PagedResponseDTO<UserResponseDTO> toPagedResponse(Page<UserResponseDTO> page);
}
