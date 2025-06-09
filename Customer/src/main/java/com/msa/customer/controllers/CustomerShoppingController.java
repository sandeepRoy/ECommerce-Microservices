package com.msa.customer.controllers;

import com.msa.customer.dtos.CreateWishlistDto;
import com.msa.customer.exceptions.customer.firstLogin.CustomerLoginException;
import com.msa.customer.model.Cart;
import com.msa.customer.model.Invoice;
import com.msa.customer.services.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/customer/cart")
public class CustomerShoppingController {
    private static final Logger log = LoggerFactory.getLogger(CustomerShoppingController.class);
    @Autowired
    public CustomerService customerService;

    @GetMapping("/get-cart")
    public ResponseEntity<Cart> getCart(
            @RequestHeader("Authorization") String access_token
    ) throws CustomerLoginException {
        Cart cart = customerService.getCart(access_token);
        return new ResponseEntity<>(cart, HttpStatus.OK);

    }

    @PutMapping("/update-cart/add-product")
    public ResponseEntity<Object> updateCart_addProduct(
            @RequestHeader("Authorization") String access_token,
            @RequestBody CreateWishlistDto createWishlistDto
    ) throws CustomerLoginException {
        Cart cart = customerService.updateCart_addProduct(access_token, createWishlistDto);
        return new ResponseEntity<>(cart, HttpStatus.OK);

    }

    @PutMapping("/update-cart/{product_name}/quantity")
    public ResponseEntity<Object> updateCartWishlistQuantity(
            @RequestHeader("Authorization") String access_token,
            @RequestParam String product_name,
            @RequestParam(required = false) Integer quantity
    ) throws CustomerLoginException {

        Cart cart = customerService.updateCart_changeQuantity(access_token, product_name, quantity);
        return new ResponseEntity<>(cart, HttpStatus.OK);

    }

    @PutMapping("/update-cart/delivery-address/{address_type}")
    public ResponseEntity<Object> updateCartDeliveryAddress(
            @RequestHeader("Authorization") String access_token,
            @PathVariable String address_type
    ) throws CustomerLoginException {

        Cart cart = customerService.updateCart_changeDeliveryAddress(access_token, address_type);
        return new ResponseEntity<Object>(cart, HttpStatus.OK);

    }

    @PutMapping("/update-cart/mode-of-payment/{payment_type}")
    public ResponseEntity<Object> updateCartModeOfPayment(
            @RequestHeader("Authorization") String access_token,
            @PathVariable String payment_type
    ) throws CustomerLoginException {
        Cart cart = customerService.updateCart_modeOfPayment(access_token, payment_type);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @DeleteMapping("/update-cart/remove/{product_name}")
    public ResponseEntity<Object> updateCart_removeProduct(
            @RequestHeader("Authorization") String access_token,
            @PathVariable String product_name) throws CustomerLoginException {

        Cart cart = customerService.updateCart_removeProduct(access_token, product_name);
        return new ResponseEntity<>(cart, HttpStatus.OK);

    }

    // i'll do it later, i don't want to create the wishlist and carts again!!!!
    @DeleteMapping("/remove-cart")
    public ResponseEntity<String> removeCart_postOrderGeneration(
            @RequestHeader("Authorization") String access_token
    ) throws CustomerLoginException {

        String response = customerService.removeCart_postOrderGeneration(access_token);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/generate-invoice")
    public ResponseEntity<Invoice> generate_invoice(
            @RequestHeader("Authorization") String access_token
    ) throws CustomerLoginException {

        Invoice invoice = customerService.generateInvoice(access_token);
        return new ResponseEntity<>(invoice, HttpStatus.OK);

    }

    @GetMapping("/get-invoice")
    public ResponseEntity<byte[]> get_invoice(
            @RequestHeader("Authorization") String access_token
    ) throws CustomerLoginException, IOException {

        byte[] invoice = customerService.getInvoice(access_token);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_PDF);
        httpHeaders.setContentDisposition(ContentDisposition.attachment().filename("invoice.pdf").build());

        return new ResponseEntity<>(invoice, HttpStatus.OK);

    }

//    @GetMapping("/download-invoice")
//    public ResponseEntity<Object> download_invoice(
//            @RequestHeader("Authorization") String access_token
//    ) throws CustomerLoginException, IOException {
//
//        byte[] pdf = customerService.downloadInvoice(access_token);
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_PDF);
//        httpHeaders.setContentDisposition(ContentDisposition.attachment().filename("invoice.pdf").build());
//
//        return new ResponseEntity<>(pdf, httpHeaders, HttpStatus.OK);
//    }
}
