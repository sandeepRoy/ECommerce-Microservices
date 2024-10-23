package com.msa.payment.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer payment_order_id;
    private String name;
    private String email;
    private String phone;
    private Double amount;
    private String order_status;
    private String razorpay_order_id;
}
