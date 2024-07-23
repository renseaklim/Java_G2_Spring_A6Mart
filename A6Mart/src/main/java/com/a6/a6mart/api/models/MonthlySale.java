package com.a6.a6mart.api.models;

import jakarta.persistence.*;

@Entity
@Table(name = "monthly_sale")
public class MonthlySale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int month;
    private int year;
    private int totalProductSold;
    private double totalExpense;
    private double totalIncome;

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

    public int getTotalProductSold() {
        return totalProductSold;
    }

    public void setTotalProductSold(int totalProductSold) {
        this.totalProductSold = totalProductSold;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }
}
