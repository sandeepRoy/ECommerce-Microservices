package com.msa.customer.dtos;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SMSRequest {
    @Nonnull
    public String phone_number; // destination number
    @Nonnull
    public String message; // body
}

