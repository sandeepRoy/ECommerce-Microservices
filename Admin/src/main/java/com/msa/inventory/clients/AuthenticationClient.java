package com.msa.inventory.clients;

import com.msa.inventory.requests.authentication.RegistrationRequest;
import com.msa.inventory.responses.authentication.AuthenticationResponse;
import com.msa.inventory.responses.profile.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "authentication", url = "http://localhost:8086/auth")
public interface AuthenticationClient {
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest registerRequest);

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody RegistrationRequest registerRequest);
}
