package com.luanr.agregadorinvestimentos.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "account_stock_tb")
@AllArgsConstructor
@NoArgsConstructor
public class AccountStock {

    @EmbeddedId
    private AccountStockId accountStockId;

    @ManyToOne
    @MapsId("account_id")
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @MapsId("stock_id")
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @Column(name = "quantity")
    private Long quantity;


}


