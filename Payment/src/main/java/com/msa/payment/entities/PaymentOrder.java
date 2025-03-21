package com.msa.payment.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payment_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer payment_order_id;
    private String name;
    private String email;
    private String phone;
    private String delivery_address;
    private Double amount;
    private String order_status;
    private String razorpay_order_id;

    private LocalDate payment_date; // payment-date
}
