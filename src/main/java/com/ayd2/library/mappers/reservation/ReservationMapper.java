package com.ayd2.library.mappers.reservation;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ayd2.library.dto.reservation.ReservationResponseDTO;
import com.ayd2.library.mappers.book.BookMapper;
import com.ayd2.library.mappers.student.StudentMapper;
import com.ayd2.library.models.reservation.Reservation;

@Mapper(componentModel = "spring", uses = {BookMapper.class, StudentMapper.class})
public interface ReservationMapper {
    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);
    
    ReservationResponseDTO toReservationResponseDTO(Reservation reservation);

}
