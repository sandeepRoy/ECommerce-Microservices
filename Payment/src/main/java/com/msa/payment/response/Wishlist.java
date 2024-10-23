package com.msa.payment.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wishlist {
    public String product_name;
    public String product_manufacturer;
    public int product_quantity;
    public double payable_amount;
}
