package com.msa.order.controllers;

import com.msa.order.entities.Order;
import com.msa.order.responses.invoicing.InvoiceResponse;
import com.msa.order.services.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/order")
public class CustomerOrderController {

    @Autowired
    CustomerOrderService customerOrderService;

    @GetMapping("/last")
    public ResponseEntity<Order> getLastOrder() {
        return new ResponseEntity<>(CustomerOrderService.order, HttpStatus.OK);
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmailInvoice(
            @RequestHeader("Authorization") String access_token,
            @RequestBody InvoiceResponse invoiceResponse
    ) throws IOException {
        String response = customerOrderService.sendEmaill(access_token, invoiceResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/send-sms")
    public ResponseEntity<String> sendTextMessage(
            @RequestHeader("Authorization") String access_token,
            @RequestBody InvoiceResponse invoiceResponse
    ) {
        String response = customerOrderService.sendSMS(access_token, invoiceResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
