package com.a6.a6mart.dao;

import com.a6.a6mart.api.models.Receipt;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ReceiptDAOImplement implements ReceiptDAO{
    private EntityManager entityManager;
    @Autowired
    public ReceiptDAOImplement(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Receipt> findLatestID() {
        TypedQuery<Receipt> query = entityManager.createQuery("SELECT COUNT(DISTINCT r.id) FROM Receipt r", Receipt.class);
        List<Receipt> result = query.getResultList();
        return result;
    }




}
