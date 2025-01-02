package com.sandeep.pdfgenerator.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrder {
    public int customer_order_id;
    public int order_id;
    public String razorpay_order_id;
    public String customer_email;
    public String customer_name;
    public String customer_phone;
    public double amount;
    public String expected_delivery_date;
    public String customer_delivery_address;
    public String status;
    public String payment_date;
    public String order_date;
    public ArrayList<CustomerPurchase> customer_purchase;
}
