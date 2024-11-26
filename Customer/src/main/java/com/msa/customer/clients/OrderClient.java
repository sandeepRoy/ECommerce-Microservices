package com.msa.customer.clients;

import com.msa.customer.responses.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "orderClient", url = "http://localhost:8089/order")
public interface OrderClient {
    @GetMapping("/get-all")
    public ResponseEntity<List<OrderResponse>> getAllOrders();
}
