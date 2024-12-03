package com.msa.order.controllers;

import com.msa.order.entities.Order;
import com.msa.order.services.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/order")
public class CustomerOrderController {

    @GetMapping("/last")
    public ResponseEntity<Order> getLastOrder() {
        return new ResponseEntity<>(CustomerOrderService.order, HttpStatus.OK);
    }
}
