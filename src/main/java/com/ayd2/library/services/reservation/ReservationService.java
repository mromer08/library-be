package com.ayd2.library.services.reservation;

import com.ayd2.library.dto.reservation.NewReservationRequestDTO;
import com.ayd2.library.dto.reservation.ReservationResponseDTO;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.exceptions.BookNotReservableException;
import com.ayd2.library.exceptions.DuplicatedEntityException;
import com.ayd2.library.exceptions.NotFoundException;
import com.ayd2.library.models.book.Book;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface ReservationService {
    ReservationResponseDTO createReservation(UUID userAccountId, NewReservationRequestDTO reservationRequestDTO)
            throws DuplicatedEntityException, NotFoundException, BookNotReservableException;

    ReservationResponseDTO getReservation(UUID id) throws NotFoundException;

    PagedResponseDTO<ReservationResponseDTO> getReservationsByAccount(UUID userAccountId, Pageable pageable)
            throws NotFoundException;

    void deleteReservation(UUID id) throws NotFoundException;

    int deleteByExpirationDate(LocalDate expirationDate);

    int updateExpirationDateByBook(Book book, LocalDate expirationDate);
}