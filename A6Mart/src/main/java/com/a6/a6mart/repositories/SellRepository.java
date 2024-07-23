package com.a6.a6mart.repositories;

import com.a6.a6mart.api.models.SellItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellRepository extends JpaRepository<SellItem, Integer> {
    @Query("SELECT s FROM SellItem s WHERE MONTH(s.date) = :month AND YEAR(s.date) = :year")
    List<SellItem> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(s.quantity) FROM SellItem s")
    int getTotalSoldQuantity();

    @Query("SELECT SUM(s.total) FROM SellItem s")
    double getTotalSalesAmount();
}
