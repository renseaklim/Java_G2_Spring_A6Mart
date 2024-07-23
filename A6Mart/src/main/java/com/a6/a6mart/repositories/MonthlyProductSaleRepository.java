package com.a6.a6mart.repositories;

import com.a6.a6mart.api.models.MonthlyProductSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlyProductSaleRepository extends JpaRepository<MonthlyProductSale, Integer> {
    @Query("SELECT m FROM MonthlyProductSale m WHERE m.month = :month AND m.year = :year")
    List<MonthlyProductSale> findByMonthAndYear(@Param("month") int month, @Param("year") int year);
}
