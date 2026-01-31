package com.msa.product.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer product_id;

    private Integer category_id;

    private String product_name;

    private String product_manufacturer;

    private String product_description;

    // replace with ENUN.IN_STOCK/OUT_OF_STOCK/DISCONTINUTED/COMMING_SOON later
    private String product_status;

    private LocalDate product_listed_on;

    private LocalDate product_updated_on;
}
