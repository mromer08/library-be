package com.ayd2.library.repositories.reservation;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd2.library.models.book.Book;
import com.ayd2.library.models.reservation.Reservation;
import com.ayd2.library.models.student.Student;

public interface ReservationRepository extends JpaRepository<Reservation, UUID>{
    boolean existsByBookAndStudent(Book book, Student student);
    Page<Reservation> findByStudent(Student student, Pageable pageable);
    int deleteByExpirationDate(LocalDate date);
}
