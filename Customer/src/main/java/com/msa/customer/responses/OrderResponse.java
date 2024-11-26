package com.msa.customer.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Integer order_id;
    private String razorpay_order_id;
    private String customer_email;
    private String customer_name;
    private String customer_phone;
    private Double amount;
    private LocalDate expected_delivery_date;
    private String customer_delivery_address;
    private String status;
}
