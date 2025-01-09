package com.msa.order.controllers;

import com.msa.order.entities.Order;
import com.msa.order.services.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

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
    public ResponseEntity<String> sendEmailInvoice() throws IOException {
        String response = customerOrderService.sendEmaill();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
