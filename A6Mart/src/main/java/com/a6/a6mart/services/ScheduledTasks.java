package com.a6.a6mart.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ScheduledTasks {

    @Autowired
    private MonthlySaleService monthlySaleService;

    @Scheduled(cron = "0 0 0 1 * ?") // Run at midnight on the first day of each month
    public void calculateMonthlySales() {
        LocalDate now = LocalDate.now().minusMonths(1); // Calculate for the previous month
        int month = now.getMonthValue();
        int year = now.getYear();

        monthlySaleService.calculateMonthlySales(month, year);
    }
}

