package com.msa.seller.clients;

import com.msa.seller.requests.authentication.RegistrationRequest;
import com.msa.seller.responses.authentication.AuthenticationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "authentication", url = "http://localhost:8086/auth")
public interface AuthenticationClient {
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest registerRequest);

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody RegistrationRequest registerRequest);
}
