package com.msa.payment.controllers;

import com.msa.payment.dtos.PaymentOrderRequest;
import com.msa.payment.entities.PaymentOrder;
import com.msa.payment.response.OrderResponse;
import com.msa.payment.services.LoadCartService;
import com.msa.payment.services.PaymentService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Logger;

@Controller
public class PaymentController {

    private static final Logger logger = Logger.getLogger(PaymentController.class.getName());


    @Autowired
    public PaymentService paymentService;

    @Autowired
    public LoadCartService loadCartService;

    public PaymentOrder paymentOrder;

    @GetMapping("/")
    public String init() {
        return "index";
    }

    // Shouldn't be listed
    @PostMapping(value = "/create-order", produces = "application/json")
    @ResponseBody
    public ResponseEntity<PaymentOrder> createPaymentOrder(@RequestBody PaymentOrderRequest paymentOrderRequest) throws RazorpayException {
        PaymentOrder paymentOrder = paymentService.createPaymentOrder(paymentOrderRequest);
        return new ResponseEntity<>(paymentOrder, HttpStatus.CREATED);
    }

    // shouldn't be listed
    @PostMapping("/handle-payment-callback")
    public String handlePaymentCallback(@RequestParam Map<String, String> responsePayload) {
        paymentOrder = paymentService.updateOrder(responsePayload);
        // Can we call Order-Service's /customer/generate-order here? We don't need to manually generate an order??
        return "success";
    }

    // to be acting as client to order-service
    // order service will use this paymentOrder to extract data and create a new order entry
    @GetMapping(value = "/get-payment-order", produces = "application/json")
    @ResponseBody
    public ResponseEntity<PaymentOrder> getPaymentOrder() {
        return new ResponseEntity<>(paymentOrder, HttpStatus.OK);
    }
}
