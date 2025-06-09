package com.msa.customer.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "emailing", url = "http://localhost:8092/email")
public interface EmailingClient {
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOTP(
            @RequestParam String to,
            @RequestParam String otp
    );
}
