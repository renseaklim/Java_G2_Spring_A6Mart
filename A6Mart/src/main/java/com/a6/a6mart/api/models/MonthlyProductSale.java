package com.a6.a6mart.api.models;

import jakarta.persistence.*;

@Entity
@Table(name = "monthly_product_sale")
public class MonthlyProductSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int month;
    private int year;
    private int productId;
    private int totalSoldQty;
    private double totalSaleMoney;

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getTotalSoldQty() {
        return totalSoldQty;
    }

    public void setTotalSoldQty(int totalSoldQty) {
        this.totalSoldQty = totalSoldQty;
    }

    public double getTotalSaleMoney() {
        return totalSaleMoney;
    }

    public void setTotalSaleMoney(double totalSaleMoney) {
        this.totalSaleMoney = totalSaleMoney;
    }
}
