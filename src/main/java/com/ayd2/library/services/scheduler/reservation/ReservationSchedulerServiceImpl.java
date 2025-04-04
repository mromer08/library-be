package com.ayd2.library.services.scheduler.reservation;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ayd2.library.exceptions.ServiceException;
import com.ayd2.library.services.reservation.ReservationService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ReservationSchedulerServiceImpl implements ReservationSchedulerService {
    private final ReservationService reservationService;

    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void processDailyReservationExpiration() throws ServiceException {
        reservationService.deleteByExpirationDate(LocalDate.now());
    }
}
