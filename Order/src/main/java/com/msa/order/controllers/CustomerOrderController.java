package com.msa.order.controllers;

import com.msa.order.entities.Order;
import com.msa.order.responses.PaymentOrderResponse;
import com.msa.order.services.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/customer")
public class CustomerOrderController {

    public static final Logger logger = Logger.getLogger(CustomerOrderController.class.getName());

    @Autowired
    public CustomerOrderService customerOrderService;

    @PostMapping("/generate-order")
    public ResponseEntity<Order> generateOrder() {
        Order order = customerOrderService.generateCustomerOrder();
        return new ResponseEntity<Order>(order, HttpStatus.CREATED);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return new ResponseEntity<>(customerOrderService.getAllOrders(), HttpStatus.OK);
    }
}
