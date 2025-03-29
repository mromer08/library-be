package com.ayd2.library.mappers.loan;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ayd2.library.dto.loan.LoanResponseDTO;
import com.ayd2.library.mappers.book.BookMapper;
import com.ayd2.library.mappers.student.StudentMapper;
import com.ayd2.library.models.loan.Loan;

@Mapper(componentModel = "spring", uses = {BookMapper.class, StudentMapper.class})
public interface LoanMapper {
    LoanMapper INSTANCE = Mappers.getMapper(LoanMapper.class);

    LoanResponseDTO toLoanResponseDTO(Loan loan);
}
