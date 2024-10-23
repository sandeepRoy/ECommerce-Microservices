package com.msa.payment.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAddress {
    public String addressType;
    public String address;
    public String city;
    public String state;
    public int pincode;
}
