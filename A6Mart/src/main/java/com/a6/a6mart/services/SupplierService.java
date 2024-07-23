package com.a6.a6mart.services;

import com.a6.a6mart.api.models.Supplier;
import com.a6.a6mart.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    public List<Supplier> getAllSupplier(){
        return supplierRepository.findAll();
    }

    public Supplier findById(int id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        if (supplier.isPresent()) {
            return supplier.get();
        } else {
            throw new RuntimeException("Supplier not found for id :: " + id);
        }
    }
}
