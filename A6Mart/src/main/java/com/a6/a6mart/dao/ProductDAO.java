package com.a6.a6mart.dao;

import com.a6.a6mart.api.models.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductDAO {
    List<Product> findOutOfStock ();

}
