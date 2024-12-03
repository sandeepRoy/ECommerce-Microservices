package com.msa.payment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrderRequest {
    private String name;
    private String email;
    private String phone;
    private Double amount;
    private String delivery_address;
}
