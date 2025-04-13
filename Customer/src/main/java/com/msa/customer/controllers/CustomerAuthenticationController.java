package com.msa.customer.controllers;

import com.msa.customer.clients.AuthenticationClient;
import com.msa.customer.clients.CachingClient;
import com.msa.customer.clients.OAuthClient;
import com.msa.customer.dtos.LoginCustomerDto;
import com.msa.customer.dtos.RegisterCustomerDto;
import com.msa.customer.exceptions.customer.secondLogin.CustomerPreviouslyLoggedInException;
import com.msa.customer.model.Customer;
import com.msa.customer.services.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
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
    public OAuthClient oAuthClient;

    @Autowired
    public CachingClient cachingClient;

    public static String TOKEN;

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody RegisterCustomerDto registerCustomerDto) {
        authenticationClient.registerUser(registerCustomerDto);
        return new ResponseEntity<>("Registration SuccessFull!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginCustomer(@RequestBody LoginCustomerDto loginCustomerDto) throws CustomerPreviouslyLoggedInException {
        TOKEN = authenticationClient.loginUser(loginCustomerDto);
        customerService.setTOKEN(TOKEN);
        customerService.addCustomer(loginCustomerDto);
        return new ResponseEntity<>("Log In SuccessFull!", HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logoutCustomer() {
        String response = customerService.logoutCustomer();
        TOKEN = "";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/social-login")
    public void socialLogin(@RequestParam String provider, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("http://localhost:8084/oauth/login?provider=" + provider);
    }

    @GetMapping("/social-callback")
    public ResponseEntity<String> socialCallback(@CookieValue(name = "JWT_TOKEN", required = false) String token) throws CustomerPreviouslyLoggedInException {
        if(token == null) {
            return new ResponseEntity<>("Social Login Failed!", HttpStatus.UNAUTHORIZED);
        }
        TOKEN = token;
        customerService.registerOrLoginOAuthUser(TOKEN);
        return new ResponseEntity<>("Social Login Successfull!", HttpStatus.OK);
    }

    @GetMapping("/get-otp")
    public void getOTP(@RequestParam(required = false) String mobile, @RequestParam(required = false) String emailId) {
        customerService.generateOTP(mobile);
    }

   // Issue: Invalid OTP(not existing in Redis is also creating a new customer, why?
   @GetMapping("/verify-otp")
    public ResponseEntity<String> get(@RequestParam String otp) {
       String response = customerService.verifyOTP(otp);
       if(response != "INVALID OTP") {
           TOKEN = response;
           customerService.setTOKEN(TOKEN);
           return new ResponseEntity<>(TOKEN, HttpStatus.OK);
       }
       else {
           return new ResponseEntity<>("INVALID OTP", HttpStatus.BAD_REQUEST);
       }
   }
}
