package com.a6.a6mart.dao;

import com.a6.a6mart.api.models.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDAOImplement implements ProductDAO{
    private EntityManager entityManager;

    @Autowired
    public ProductDAOImplement(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Product> findOutOfStock() {
        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p WHERE p.quantity = 0", Product.class);
        List<Product> result = query.getResultList();
        return result;
    }
}
