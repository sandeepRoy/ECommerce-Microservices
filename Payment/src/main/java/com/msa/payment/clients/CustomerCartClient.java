package com.msa.payment.clients;

import com.msa.payment.response.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "customer", url = "http://localhost:8087/customer")
public interface CustomerCartClient {

    @GetMapping("/cart/get-cart")
    public ResponseEntity<CartResponse> getCustomerCart();
}