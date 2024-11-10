package com.msa.order.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrderResponse {
    private Integer payment_order_id;
    private String name;
    private String email;
    private String phone;
    private String delivery_address;
    private Double amount;
    private String order_status;
    private String razorpay_order_id;
}
