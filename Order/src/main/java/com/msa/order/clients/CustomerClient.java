package com.msa.order.clients;

import com.msa.order.responses.CartResponse;
import com.msa.order.responses.invoicing.InvoiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "customer", url = "http://localhost:8087/customer")
public interface CustomerClient {

    @GetMapping("/cart/get-cart")
    public ResponseEntity<CartResponse> getCustomerCart(@RequestHeader("Authorization") String access_token);

    @DeleteMapping("/cart/remove-cart")
    public ResponseEntity<Object> removeCart_postOrderGeneration(@RequestHeader("Authorization") String access_token);

    @GetMapping("/cart/get-invoice")
    public ResponseEntity<byte[]> get_invoice(@RequestHeader("Authorization") String access_token);
}
