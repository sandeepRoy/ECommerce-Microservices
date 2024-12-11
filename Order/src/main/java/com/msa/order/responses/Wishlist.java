package com.msa.order.responses;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Wishlist {
    public String product_name;
    public String product_manufacturer;
    public int product_quantity;
    public double payable_amount;
}
