package com.ayd2.library.services.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ayd2.library.dto.configurations.ConfigurationResponseDTO;
import com.ayd2.library.exceptions.ServiceException;
import com.ayd2.library.models.loan.Loan;
import com.ayd2.library.repositories.loan.LoanRepository;
import com.ayd2.library.services.configuration.ConfigurationService;
import com.ayd2.library.services.loan.LoanService;
import com.ayd2.library.specifications.loan.LoanSpecs;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class LoanSchedulerServiceImpl implements LoanSchedulerService{
    private final LoanService loanService;
    private final LoanRepository loanRepository;
    private final ConfigurationService configurationService;

        @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    @Override
    public void processDailyLoanCalculate() throws ServiceException{
        ConfigurationResponseDTO config = configurationService.getConfiguration();
        LocalDate today = LocalDate.now();
        List<Loan> activeLoans = loanRepository.findAll(LoanSpecs.isNotReturned());

        for (Loan loan : activeLoans) {
            loanService.calculatePayment(loan, today, config);
        }
    }
}
