package com.msa.product.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductDto {

    private Integer category_id;

    private String product_name;

    private String product_manufacturer;

    private String product_description;

    // replace with ENUN.IN_STOCK/OUT_OF_STOCK/DISCONTINUTED/COMMING_SOON later
    private String product_status;

    private LocalDate product_listed_on;

    private LocalDate product_updated_on;
}
