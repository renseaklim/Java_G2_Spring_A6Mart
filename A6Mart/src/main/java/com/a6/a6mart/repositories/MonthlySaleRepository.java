package com.a6.a6mart.repositories;

import com.a6.a6mart.api.models.MonthlySale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MonthlySaleRepository extends JpaRepository<MonthlySale, Integer> {
    @Query("SELECT m FROM MonthlySale m WHERE m.month = :month AND m.year = :year")
    MonthlySale findByMonthAndYear(@Param("month") int month, @Param("year") int year);
}