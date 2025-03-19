package com.msa.authentication.controller;

import com.msa.authentication.entities.User;
import com.msa.authentication.repositories.UserRepository;
import com.msa.authentication.requests.UpdateNameRequest;
import com.msa.authentication.responses.AuthResponse;
import com.msa.authentication.requests.AuthenticateRequest;
import com.msa.authentication.services.AuthenticationService;
import com.msa.authentication.requests.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/auth")
public class AuthController {

    public Logger logger = Logger.getLogger(AuthController.class.getName());

    @Autowired
    public AuthenticationService authenticationService;

    @Autowired
    public UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthenticateRequest authenticateRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticateRequest));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> delete(@RequestParam String token) {
        String response = authenticationService.remove(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public String logout() {
        return "Logged Out!";
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestHeader("Authorization") String token) {
        Boolean response = authenticationService.validateToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/otp-login")
    public ResponseEntity<AuthResponse> otpLogin(@RequestParam String email) {
        return ResponseEntity.ok(authenticationService.otpLogin(email));
    }

    @PutMapping("/update-name")
    public ResponseEntity<String> update(@RequestParam String email, @RequestBody UpdateNameRequest updateNameRequest) {
        return ResponseEntity.ok(authenticationService.updateName(email, updateNameRequest));
    }
}
