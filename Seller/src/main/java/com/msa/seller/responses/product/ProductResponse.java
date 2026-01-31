package com.msa.seller.responses.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Integer product_id;

    private String product_name;

    private String product_manufacturer;

    private Long product_price;

    private Integer product_in_stock;

    private Integer category_id;
}
