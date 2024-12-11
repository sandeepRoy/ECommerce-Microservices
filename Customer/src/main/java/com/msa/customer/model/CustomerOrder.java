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




//     Issue: I can assign Wishlist items to Customer order,
//            but after order is generated, wishlist should be deleted for newer item purchase
//
//            If i delete wishlist, then customer order won't show wishlist items
//            that same goes for cart as well, since cart is going to be removed after customer_order generation
//
//            What is the best way?
//
//     ----------------------------------------------------------------------------------------------------------------------------
//     Solution 1: Let's try with creating a separate table name purchased_items, which will get populated with wishlist items data
//                 then assign it as a @OneToMany with 'customer_order'(for order processing) and 'customer'(for view) table
//
//                 Doing this, we can have a record of items purchased, and a generated order as well.
//
//     Attempt 1: 11 - 12 - 2024 07:30 PM IST

}
