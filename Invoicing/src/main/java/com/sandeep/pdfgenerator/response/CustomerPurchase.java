package com.sandeep.pdfgenerator.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPurchase {
    public int customer_purchase_id;
    public String product_name;
    public String product_manufacturer;
    public int product_quantity;
    public double product_price;
}
