package com.a6.a6mart.dao;

import com.a6.a6mart.api.models.Receipt;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptDAO {
    public List<Receipt> findLatestID();
}
