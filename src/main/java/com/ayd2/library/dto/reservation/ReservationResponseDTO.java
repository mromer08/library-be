package com.ayd2.library.dto.reservation;

import java.time.LocalDate;
import java.util.UUID;

import com.ayd2.library.dto.books.BookResponseDTO;
import com.ayd2.library.dto.students.StudentResponseDTO;

public record ReservationResponseDTO(
    UUID id,
    BookResponseDTO book,
    StudentResponseDTO student,
    LocalDate reservationDate
) {}
