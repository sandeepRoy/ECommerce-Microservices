package com.msa.order.clients;

import com.msa.order.responses.PaymentOrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@FeignClient(name = "payment", url = "http://localhost:8088")
public interface PaymentClient {
    @GetMapping(value = "/get-payment-order", produces = "application/json")
    @ResponseBody
    public ResponseEntity<PaymentOrderResponse> getPaymentOrder();
}
