package com.luanr.agregadorinvestimentos.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class AccountStockId {

    @Column(name = "account_id")
    private UUID account_id;


    @Column(name = "stock_id")
    private String stock_id;

}


