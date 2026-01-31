package com.msa.seller.controllers;

import com.msa.seller.requests.authentication.RegistrationRequest;
import com.msa.seller.responses.authentication.AuthenticationResponse;
import com.msa.seller.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerAuthenticationController {
    @Autowired
    public AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegistrationRequest registrationRequest) {
        return new ResponseEntity<>(authenticationService.registerSeller(registrationRequest), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginAdmin(@RequestBody RegistrationRequest registrationRequest) {
        return new ResponseEntity<>(authenticationService.loginSeller(registrationRequest), HttpStatus.OK);
    }
}
