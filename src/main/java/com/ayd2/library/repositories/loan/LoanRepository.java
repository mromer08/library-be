package com.ayd2.library.repositories.loan;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ayd2.library.models.loan.Loan;
import com.ayd2.library.models.student.Student;

public interface LoanRepository extends JpaRepository<Loan, UUID>, JpaSpecificationExecutor<Loan> {
    long countByStudentAndReturnDateIsNull(Student student);
    Page<Loan> findByDueDate(LocalDate duedate, Pageable pageable);
}