package com.luanr.agregadorinvestimentos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stocks_tb")
public class Stock {

    @Id
    @Column(name = "stock_id")
    private String stockId;

    @Column(name = "description")
    private String description;

    @Column (name = "currency")
    private String currency;

    @Column(name = "last_price")
    private Double lastPrice;

    public Stock(String stockId, String description, String currency) {
        this.stockId = stockId;
        this.description = description;
        this.currency = currency;
        this.lastPrice = null;
    }
}
