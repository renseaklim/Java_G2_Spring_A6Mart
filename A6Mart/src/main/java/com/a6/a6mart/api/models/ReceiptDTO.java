package com.a6.a6mart.api.models;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.List;

public class ReceiptDTO {
    private List<SellItemDTO> items;
    private double total;

    // Getters and Setters

    public List<SellItemDTO> getItems() {
        return items;
    }

    public void setItems(List<SellItemDTO> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}