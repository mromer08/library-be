package com.ayd2.library.services.scheduler.reservation;

import com.ayd2.library.exceptions.ServiceException;

public interface ReservationSchedulerService {

    void processDailyReservationExpiration() throws ServiceException;
}
