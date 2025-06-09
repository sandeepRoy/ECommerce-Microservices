package com.msa.authentication.controller;

import com.msa.authentication.requests.AuthenticateRequest;
import com.msa.authentication.requests.RegisterRequest;
import com.msa.authentication.responses.AuthResponse;
import com.msa.authentication.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    public AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        AuthResponse authResponse = authenticationService.register(registerRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthenticateRequest authenticateRequest) {
        AuthResponse authResponse = authenticationService.authenticate(authenticateRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/otp-login")
    public ResponseEntity<AuthResponse> otpLogin(@RequestParam String email) {
        AuthResponse authResponse = authenticationService.otpLogin(email);
        return  new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest httpRequest) {
        log.info(httpRequest.getHeader("Authorization"));                                                         // takes the refresh_token as Authorization
        String authorizationHeader = httpRequest.getHeader("Authorization");                                      // extract the httpHeader part

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("authorizationHeader: " + authorizationHeader);                                                    // check for blank token or token w/o Bearer
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        AuthResponse authResponse = authenticationService
                .refreshAccessToken(                                                                                    // token sent to authenticationService
                        authorizationHeader.substring(                                                                  // token without
                                7                                                                                       // Bearer
                        )
                );
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
