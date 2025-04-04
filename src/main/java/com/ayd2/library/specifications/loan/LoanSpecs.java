package com.ayd2.library.specifications.loan;

import com.ayd2.library.models.loan.Loan;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class LoanSpecs {

    public static Specification<Loan> hasStudentId(UUID studentId) {
        return (root, query, criteriaBuilder) -> 
            studentId == null ? null : criteriaBuilder.equal(root.get("student").get("id"), studentId);
    }

    public static Specification<Loan> hasBookId(UUID bookId) {
        return (root, query, criteriaBuilder) -> 
            bookId == null ? null : criteriaBuilder.equal(root.get("book").get("id"), bookId);
    }

    public static Specification<Loan> hasStudentCarnet(Long studentCarnet) {
        return (root, query, criteriaBuilder) -> 
            studentCarnet == null ? null : criteriaBuilder.equal(root.get("student").get("carnet"), studentCarnet);
    }

    public static Specification<Loan> hasBookCode(String bookCode) {
        return (root, query, criteriaBuilder) -> 
            bookCode == null ? null : criteriaBuilder.equal(root.get("book").get("code"), bookCode);
    }

    public static Specification<Loan> loanDateBefore(LocalDate date) {
        return (root, query, criteriaBuilder) -> 
            date == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("loanDate"), date);
    }

    public static Specification<Loan> loanDateAfter(LocalDate date) {
        return (root, query, criteriaBuilder) -> 
            date == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("loanDate"), date);
    }

    public static Specification<Loan> dueDateBefore(LocalDate date) {
        return (root, query, criteriaBuilder) -> 
            date == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), date);
    }

    public static Specification<Loan> dueDateAfter(LocalDate date) {
        return (root, query, criteriaBuilder) -> 
            date == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), date);
    }

    public static Specification<Loan> isNotReturned() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.isNull(root.get("returnDate"));
    }

    public static Specification<Loan> isReturned() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.isNotNull(root.get("returnDate"));
    }

    public static Specification<Loan> hasDueDate(LocalDate dueDate) {
        return (root, query, criteriaBuilder) -> 
            dueDate == null ? null : criteriaBuilder.equal(root.get("dueDate"), dueDate);
    }

    public static Specification<Loan> returnDateBefore(LocalDate date) {
        return (root, query, criteriaBuilder) -> 
            date == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("returnDate"), date);
    }
    
    public static Specification<Loan> returnDateAfter(LocalDate date) {
        return (root, query, criteriaBuilder) -> 
            date == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("returnDate"), date);
    }
}
