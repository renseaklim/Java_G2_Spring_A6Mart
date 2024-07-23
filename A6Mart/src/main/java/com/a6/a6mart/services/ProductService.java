package com.a6.a6mart.services;

import com.a6.a6mart.api.models.Category;
import com.a6.a6mart.api.models.Product;
import com.a6.a6mart.repositories.CategoryRepository;
import com.a6.a6mart.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }
    public Product findById(int id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new RuntimeException("product not found for id :: " + id);
        }
    }
}
