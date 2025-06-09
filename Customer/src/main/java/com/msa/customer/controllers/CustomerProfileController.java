package com.msa.customer.controllers;

import com.msa.customer.dtos.*;
import com.msa.customer.exceptions.address.add.AddressAdditionException;
import com.msa.customer.exceptions.address.update.AddressUpdateException;
import com.msa.customer.exceptions.customer.firstLogin.CustomerLoginException;
import com.msa.customer.exceptions.customer.secondLogin.CustomerPreviouslyLoggedInException;
import com.msa.customer.model.BuyLater;
import com.msa.customer.model.Cart;
import com.msa.customer.model.Customer;
import com.msa.customer.model.CustomerOrder;
import com.msa.customer.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/customer/profile")
public class CustomerProfileController {
    public Logger logger = Logger.getLogger(CustomerProfileController.class.getName());

    @Autowired
    public CustomerService customerService;

    @PostMapping("/new")
    public ResponseEntity<Customer> createNewCustomerProfile(
            @RequestHeader("Authorization") String token
    ) throws CustomerPreviouslyLoggedInException {
        Customer customer = customerService.addCustomer(token);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }


    @GetMapping("/details")
    public ResponseEntity<Customer> getCustomerProfile(
            @RequestHeader("Authorization") String token
    ) throws CustomerLoginException {
        Customer customerProfile = customerService.getCustomerProfile(token);
        return new ResponseEntity<>(customerProfile, HttpStatus.OK);
    }

    @PutMapping("/update/personal-details")
    public ResponseEntity<Customer> updateCustomerProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdateCustomerProfileDto updateCustomerProfileDto
    ) throws CustomerLoginException {
            Customer customer = customerService.updateCustomerProfile(token, updateCustomerProfileDto);
            return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping("/add-address")
    public ResponseEntity<Customer> addAddressForCustomer(
            @RequestHeader("Authorization") String access_token,
            @RequestBody AddressAddDto addressAddDto
    ) throws CustomerLoginException, AddressAdditionException {
        Customer customer = customerService.addAddressToCustomer(access_token, addressAddDto);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @PutMapping("/update-address/{address_type}")
    public ResponseEntity<Customer> updateAddressOfCustomer(
            @RequestHeader("Authorization") String access_token,
            @PathVariable String address_type,
            @RequestBody UpdateAddressDto updateAddressDto
    ) throws CustomerLoginException, AddressUpdateException {
        Customer customer = customerService.updateAddressOfCustomer(access_token, address_type, updateAddressDto);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @DeleteMapping("/remove-address/{address_type}")
    public ResponseEntity<Customer> deleteAddressOfCustomer(
            @RequestHeader("Authorization") String access_token,
            @PathVariable String address_type
    ) throws CustomerLoginException {
        Customer customer = customerService.deleteAddressOfCustomer(access_token, address_type);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }


    @PostMapping("/add-to-buylater")
    public ResponseEntity<BuyLater> addBuyLater_newProduct(
            @RequestHeader("Authorization") String access_token,
            @RequestBody CreateWishlistDto createWishlistDto
    ) throws CustomerLoginException {
        BuyLater buyLater = customerService.addBuyLater_newProduct(access_token, createWishlistDto);
        return new ResponseEntity<>(buyLater, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCustomer(
            @RequestHeader("Authorization") String access_token
    ) throws CustomerLoginException {
        String response = customerService.deleteCustomer(access_token);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PutMapping("/add-buylater-to-cart")
    public ResponseEntity<Cart> addToCart_buyLater(
            @RequestHeader("Authorization") String access_token
    ) throws CustomerLoginException {
        Cart cart = customerService.updateCart_addBuyLater(access_token);
        return new ResponseEntity<>(cart, HttpStatus.OK);

    }

    @PutMapping("/fetch-orders")
    public ResponseEntity<Customer> addOrders_toCustomerProfile(
            @RequestHeader("Authorization") String access_token
    ) throws CustomerLoginException {
        CustomerOrder customerOrder = customerService.fetchOrders_fromOrderService(access_token);
        Customer customer = customerService.addWishlist_toCustomerOrder(access_token, customerOrder);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping("/update-email/send-otp")
    public ResponseEntity<String> sendOTPToEmail(
            @RequestHeader("Authorization") String access_token,
            @RequestParam(required = false) String email
    ) throws CustomerLoginException {
        customerService.sendOTPToEmail(access_token, email);
        return new ResponseEntity<String>("OTP SENT TO EMAIL!", HttpStatus.OK);
    }

    @PutMapping("/update-email/verify-otp")
    public ResponseEntity<Customer> verifyOTPToEmail(
            @RequestHeader("Authorization") String access_token,
            @RequestParam String otp
    ) throws CustomerLoginException {
        Customer customer = customerService.verifyEmailOTP(access_token, otp);
        return new  ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PutMapping("/update-mobile/send-otp")
    public ResponseEntity<String> sendOTPToMobile(
            @RequestHeader("Authorization") String access_token,
            @RequestParam(required = false) String mobile
    ) throws CustomerLoginException {
        customerService.sendOTPToMobile(access_token, mobile);
        return new ResponseEntity<String>("OTP SENT TO Mobile!", HttpStatus.OK);
    }

    @PutMapping("/update-mobile/verify-otp")
    public ResponseEntity<Customer> verifyOTPToMobile(
            @RequestHeader("Authorization") String access_token,
            @RequestParam String otp
    ) throws CustomerLoginException {
        Customer customer = customerService.verifyMobileOTP(access_token, otp);
        return new  ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Customer> changePassword(
            @RequestHeader("Authorization") String access_token,
            @RequestBody UpdatePasswordDto updatePasswordDto
    ) throws CustomerLoginException {
        Customer customer = customerService.changePassword(access_token, updatePasswordDto);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}

