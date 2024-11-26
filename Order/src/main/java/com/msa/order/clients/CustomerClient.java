package com.msa.order.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;

@FeignClient(name = "customerClient", url = "http://localhost:8087/customer/")
public interface CustomerClient {

    @DeleteMapping("/cart/remove")
    public ResponseEntity<Object> deleteCart_afterSuccesfulOrderGeneration();
}
