package com.msa.order.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "`order`")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
