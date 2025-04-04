package com.ayd2.library.services.reservation;

import com.ayd2.library.dto.reservation.*;
import com.ayd2.library.exceptions.*;
import com.ayd2.library.dto.generic.PagedResponseDTO;
import com.ayd2.library.mappers.reservation.ReservationMapper;
import com.ayd2.library.mappers.generic.GenericPageMapper;
import com.ayd2.library.models.book.Book;
import com.ayd2.library.models.reservation.Reservation;
import com.ayd2.library.models.student.Student;
import com.ayd2.library.repositories.book.BookRepository;
import com.ayd2.library.repositories.reservation.ReservationRepository;
import com.ayd2.library.repositories.student.StudentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final ReservationMapper reservationMapper;
    private final GenericPageMapper reservationPageMapper;

    @Override
    public ReservationResponseDTO createReservation(UUID userAccountId, NewReservationRequestDTO reservationRequestDTO)
            throws DuplicatedEntityException, NotFoundException {

        Book book = bookRepository.findById(reservationRequestDTO.bookId())
                .orElseThrow(() -> new NotFoundException("Book not found with id: " + reservationRequestDTO.bookId()));

        Student student = studentRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> new NotFoundException("Student not found with user account: " + userAccountId));

        if (reservationRepository.existsByBookAndStudent(book, student)) {
            throw new DuplicatedEntityException("The student already has an active reservation for this book");
        }

        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setStudent(student);
        reservation.setReservationDate(LocalDate.now());
        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toReservationResponseDTO(savedReservation);
    }

    @Override
    public ReservationResponseDTO getReservation(UUID id) throws NotFoundException {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found with id: " + id));
        return reservationMapper.toReservationResponseDTO(reservation);
    }

    @Override
    public PagedResponseDTO<ReservationResponseDTO> getReservationsByAccount(UUID userAccountId, Pageable pageable)
            throws NotFoundException {
        Student student = studentRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> new NotFoundException("Student not found with user account: " + userAccountId));
        Page<Reservation> page = reservationRepository.findByStudent(student, pageable);
        Page<ReservationResponseDTO> dtoPage = page.map(reservationMapper::toReservationResponseDTO);
        return reservationPageMapper.toPagedResponse(dtoPage);
    }

    @Override
    public void deleteReservation(UUID id) throws NotFoundException {
        if (!reservationRepository.existsById(id)) {
            throw new NotFoundException("Reservation not found with id: " + id);
        }
        reservationRepository.deleteById(id);
    }

    @Override
    public int deleteByExpirationDate(LocalDate expirationDate){
        return reservationRepository.deleteByExpirationDate(expirationDate);
    }
}