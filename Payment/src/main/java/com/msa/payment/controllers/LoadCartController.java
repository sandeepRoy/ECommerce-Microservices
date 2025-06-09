package com.msa.payment.controllers;

import com.msa.payment.response.CartResponse;
import com.msa.payment.services.LoadCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class LoadCartController {
    private static CartResponse cartResponse;

    @Autowired
    public LoadCartService loadCartService;

    @GetMapping("/load-cart")
    public ResponseEntity<CartResponse> loadCart(@RequestHeader("Authorization") String access_token) {
        cartResponse = loadCartService.loadCart(access_token);
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

    // 1. A ui page is made that loads the cart data
    // 2. Another controller(separate) is required to create an order
    // 3. Make another controller "payment/createOrder" that will take the ui data, and create the order
    // 4. refer how Ashok Sir has created the page and event to call the POST request to call the /createOrder
}
