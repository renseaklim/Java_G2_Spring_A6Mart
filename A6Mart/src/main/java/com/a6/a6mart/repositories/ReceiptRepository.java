package com.a6.a6mart.repositories;

import com.a6.a6mart.api.models.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {
    @Query("SELECT COUNT(DISTINCT r.id) FROM Receipt r")
    int countDistinctReceiptIds();
}
