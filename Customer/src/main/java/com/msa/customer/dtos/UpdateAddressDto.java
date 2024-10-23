package com.msa.customer.dtos;

import com.msa.customer.model.AddressType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAddressDto {
    private String address;

    private String city;

    private String state;

    private Integer pincode;
}
