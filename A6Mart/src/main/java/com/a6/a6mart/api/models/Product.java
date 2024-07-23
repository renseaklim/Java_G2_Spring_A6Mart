package com.a6.a6mart.api.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String status;
    ////////////////////////////////////////////////
    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    /////////////////////////////////////////////
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockIn> stockins;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockOut> stockouts;
    @OneToMany(mappedBy = "product")
    private List<ReceiptItem> receiptItems;

    public Integer getCategory_id(){
        return category != null ? category.getId() : null;
    }

    public Integer getSupplierId() {
        return supplier != null ? supplier.getId() : null;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<StockIn> getStockins() {
        return stockins;
    }

    public void setStockins(List<StockIn> stockins) {
        this.stockins = stockins;
    }

    public List<StockOut> getStockouts() {
        return stockouts;
    }

    public void setStockouts(List<StockOut> stockouts) {
        this.stockouts = stockouts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
