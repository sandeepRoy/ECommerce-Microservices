package com.msa.customer.clients;

import com.msa.customer.model.Invoice;
import com.msa.customer.responses.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "orderClient", url = "http://localhost:8089/order")
public interface OrderClient {
    @GetMapping("/last")
    public ResponseEntity<OrderResponse> getLastOrder(); // fetches the last order

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmailInvoice(
            @RequestHeader("Authorization") String access_token,
            @RequestBody Invoice invoice
    );

    @PostMapping("/send-sms")
    public ResponseEntity<String> sendTextMessage(
            @RequestHeader("Authorization") String access_token,
            @RequestBody Invoice invoice
    );
}
