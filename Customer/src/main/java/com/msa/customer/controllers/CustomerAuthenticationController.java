package com.msa.customer.controllers;

import com.msa.customer.clients.AuthenticationClient;
import com.msa.customer.clients.LogoutClient;
import com.msa.customer.dtos.LoginCustomerDto;
import com.msa.customer.dtos.RegisterCustomerDto;
import com.msa.customer.exceptions.customer.secondLogin.CustomerPreviouslyLoggedInException;
import com.msa.customer.responses.AuthResponse;
import com.msa.customer.responses.OTPResponse;
import com.msa.customer.services.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/customer/auth")
public class CustomerAuthenticationController {

    public Logger logger = Logger.getLogger(CustomerAuthenticationController.class.getName());

    @Autowired
    public CustomerService customerService;

    @Autowired
    public AuthenticationClient authenticationClient;

    @Autowired
    public LogoutClient logoutClient;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerCustomer(@RequestBody RegisterCustomerDto registerCustomerDto) {
        return authenticationClient.registerUser(registerCustomerDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginCustomer(@RequestBody LoginCustomerDto loginCustomerDto) throws CustomerPreviouslyLoggedInException {
        return authenticationClient.loginUser(loginCustomerDto);
    }

    @GetMapping("/social-login")
    public void socialLogin(@RequestParam String provider, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("http://localhost:8084/oauth/login?provider=" + provider);
    }

    @GetMapping("/social-callback")
    public ResponseEntity<AuthResponse> socialCallback(
            @CookieValue(name = "ACCESS_TOKEN", required = false) String access_token,
            @CookieValue(name = "REFRESH_TOKEN", required = false) String refresh_token
    ) throws CustomerPreviouslyLoggedInException {
        if(access_token == null || refresh_token == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        AuthResponse authResponse = AuthResponse.builder().access_token(access_token).refresh_token(refresh_token).build();
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @GetMapping("/get-otp")
    public ResponseEntity<OTPResponse> getOTP(@RequestParam(required = false) String mobile, @RequestParam(required = false) String emailId) {
        // need a check for mobile : email if present
        // need to send OTP to email
        // need a service method for OTP validation, user & customer creation
        OTPResponse otpResponse = customerService.generateOTP(mobile);
        return new ResponseEntity<>(otpResponse, HttpStatus.OK);
    }

   @GetMapping("/verify-otp")
   public ResponseEntity<AuthResponse> get(@RequestParam String otp) {
        AuthResponse authResponse = customerService.verifyOTP(otp);
        if (authResponse.getAccess_token() != "INVALID OTP") {
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
        }
   }

   @PostMapping("/refresh")
   public ResponseEntity<AuthResponse> refresh(@RequestHeader("Authorization") String refresh_token) {
        logger.info("Refresh Token :" + refresh_token);
        return authenticationClient.refresh(refresh_token);
   }

   @PostMapping("/logout")
   public ResponseEntity<String> logout(@RequestHeader("Authorization") String access_token, @RequestBody String refresh_token) {
        return logoutClient.invoke(access_token, refresh_token);
   }
}
