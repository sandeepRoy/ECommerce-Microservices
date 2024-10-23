package com.msa.payment.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    public double total_amount;
    public String modeOfPayment;
    public String customer_name;
    public String customer_email;
    public String customer_mobile;
    public String customer_gender;
    public ArrayList<Wishlist> wishlist;
    public DeliveryAddress delivery_address;
}
