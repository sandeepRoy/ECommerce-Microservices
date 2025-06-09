package com.msa.customer.clients;

import com.msa.customer.dtos.LoginCustomerDto;
import com.msa.customer.dtos.RegisterCustomerDto;
import com.msa.customer.dtos.UpdateNameDto;
import com.msa.customer.responses.AuthResponse;
import com.msa.customer.responses.UserProfileResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "authentication", url = "http://localhost:8084/auth")
public interface AuthenticationClient {
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterCustomerDto registerCustomerDto);

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginCustomerDto loginCustomerDto);

    @PostMapping("/otp-login")
    public ResponseEntity<AuthResponse> otpLogin(@RequestParam String email);

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader("Authorization") String refresh_token);
}
