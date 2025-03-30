package com.ayd2.library.services.scheduler;

import com.ayd2.library.exceptions.ServiceException;

public interface LoanSchedulerService {
    void processDailyLoanCalculate() throws ServiceException;
}
