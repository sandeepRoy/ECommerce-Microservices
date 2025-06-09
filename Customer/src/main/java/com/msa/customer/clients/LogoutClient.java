package com.msa.customer.clients;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "logout", url = "http://localhost:8084/logout")
public interface LogoutClient {

    @PostMapping("/invoke")
    public ResponseEntity<String> invoke(@RequestHeader("Authorization") String access_token, @RequestBody String refresh_token);
}
