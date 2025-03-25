package com.ayd2.library.mappers.book;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.ayd2.library.dto.books.BookResponseDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;

@Mapper
public interface BookPageMapper {

    BookPageMapper INSTANCE = Mappers.getMapper(BookPageMapper.class);

    @Mapping(target = "data", source = "content")
    @Mapping(target = "pageNumber", source = "number")
    @Mapping(target = "isFirst", expression = "java(page.isFirst())")
    @Mapping(target = "isLast", expression = "java(page.isLast())")
    @Mapping(target = "hasNext", expression = "java(page.hasNext())")
    @Mapping(target = "hasPrevious", expression = "java(page.hasPrevious())")
    PagedResponseDTO<BookResponseDTO> toPagedResponse(Page<BookResponseDTO> page);
}
