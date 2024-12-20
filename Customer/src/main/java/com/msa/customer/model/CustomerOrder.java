package com.msa.customer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customer_order_id;
    private Integer order_id;
    private String razorpay_order_id;
    private String customer_email;
    private String customer_name;
    private String customer_phone;
    private Double amount;
    private LocalDate expected_delivery_date;
    private String customer_delivery_address;
    private String status;

    @OneToMany(mappedBy = "customer_order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CustomerPurchase> customer_purchase = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;
}
