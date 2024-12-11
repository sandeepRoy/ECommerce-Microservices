package com.msa.customer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_purchase")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customer_purchase_id;
    private String product_name;
    private String product_manufacturer;
    private Integer product_quantity;
    private Double product_price;

    @ManyToOne
    @JoinColumn(name = "customer_order_id")
    @JsonIgnore
    private CustomerOrder customer_order;
}
