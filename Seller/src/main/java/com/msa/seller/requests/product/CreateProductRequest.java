package com.msa.seller.requests.product;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {

    // table - product
    private Integer category_id;
    private String product_name;
    private String product_manufacturer;
    private String product_description;
    private String product_status;
    private LocalDate product_listed_on;
    private LocalDate product_updated_on;


    // get created product_id and map it in seller table

    // table - seller : product_id, product_in_stock, product_price
    private Integer product_in_stock;
    private Long product_price;
}
