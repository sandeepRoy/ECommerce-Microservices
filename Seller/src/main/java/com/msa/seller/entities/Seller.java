package com.msa.seller.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seller")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seller_id;

    private String seller_email;

    private Integer product_id;

    private Integer product_in_stock;

    private Long product_price;
}
