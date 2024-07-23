package com.a6.a6mart.repositories;

import com.a6.a6mart.api.models.StockIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockinRepository extends JpaRepository<StockIn, Integer> {
    @Query("SELECT s FROM StockIn s WHERE MONTH(s.date) = :month AND YEAR(s.date) = :year")
    List<StockIn> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(s.quantity * s.cost) FROM StockIn s")
    double getTotalStockInAmount();
}
