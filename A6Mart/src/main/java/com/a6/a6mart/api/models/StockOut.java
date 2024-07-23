package com.a6.a6mart.api.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "stock_out")
public class StockOut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    public Integer getProduct_id()
    {
        return product != null ? product.getId() : null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
