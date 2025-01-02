package com.msa.order.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrder {
    @JsonProperty("payment_order_id")
    private Integer payment_order_id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("delivery_address")
    private String delivery_address;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("order_status")
    private String order_status;
    @JsonProperty("razorpay_order_id")
    private String razorpay_order_id;
    @JsonProperty("payment_date")
    private LocalDate payment_date;
}

