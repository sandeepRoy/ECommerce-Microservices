package com.msa.order.requests.sms;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SMSRequest {
    @Nonnull
    public String phone_number; // destination number
    @Nonnull
    public String message; // body
}

