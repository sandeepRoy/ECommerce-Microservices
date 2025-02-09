package com.msa.customer.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "oauth", url = "http://localhost:8084/oauth")
public interface OAuthClient {
    @GetMapping("/login")
    public ResponseEntity<String> socialLogin(@RequestParam String provider);
}
