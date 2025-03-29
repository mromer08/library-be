package com.ayd2.library.repositories.loan;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd2.library.models.loan.Loan;
import com.ayd2.library.models.student.Student;

public interface LoanRepository extends JpaRepository<Loan, UUID>{
    long countByStudentAndReturnDateIsNull(Student student);

}