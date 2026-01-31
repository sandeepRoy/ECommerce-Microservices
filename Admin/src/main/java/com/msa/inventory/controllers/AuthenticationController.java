package com.msa.inventory.controllers;

import com.msa.inventory.requests.authentication.RegistrationRequest;
import com.msa.inventory.responses.authentication.AuthenticationResponse;
import com.msa.inventory.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AuthenticationController {
    @Autowired
    public AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegistrationRequest registrationRequest) {
        return new ResponseEntity<>(authenticationService.registerAdmin(registrationRequest), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginAdmin(@RequestBody RegistrationRequest registrationRequest) {
        return new ResponseEntity<>(authenticationService.loginAdmin(registrationRequest), HttpStatus.OK);
    }
}
