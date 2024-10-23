package com.msa.payment.services;

import com.msa.payment.clients.CustomerCartClient;
import com.msa.payment.response.CartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoadCartService {

    @Autowired
    public CustomerCartClient customerCartClient;

    public CartResponse loadCart() {
        CartResponse cartResponse = customerCartClient.getCustomerCart().getBody();
        return cartResponse;
    }
}
