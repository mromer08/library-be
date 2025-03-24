package com.ayd2.library.mappers.book;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.ayd2.library.dto.books.*;
import com.ayd2.library.models.book.Book;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookResponseDTO toBookResponseDTO(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    Book toBook(NewBookRequestDTO bookRequestDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateBookFromDTO(UpdateBookRequestDTO dto, @MappingTarget Book book);
    
}
