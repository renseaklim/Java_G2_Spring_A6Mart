package com.a6.a6mart.repositories;

import com.a6.a6mart.api.models.StockOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockoutRepository extends JpaRepository<StockOut, Integer> {

}
