package com.ayd2.library.mappers.book;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.ayd2.library.dto.books.*;
import com.ayd2.library.models.book.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(source = "author.name", target = "author")
    BookResponseDTO toBookResponseDTO(Book book);
    
    BookResponseDetailDTO toBookResponseDetailDTO(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "availableCopies", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    Book toBook(NewBookRequestDTO bookRequestDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, ignoreByDefault = true)
    @Mapping(target = "title")
    @Mapping(target = "quantity")
    @Mapping(target = "publicationDate")
    @Mapping(target = "price")
    void updateBookFromDTO(UpdateBookRequestDTO dto, @MappingTarget Book book);
    
}
