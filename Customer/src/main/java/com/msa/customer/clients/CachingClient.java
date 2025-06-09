package com.msa.customer.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "caching", url = "http://localhost:8099/cache")
public interface CachingClient {
    @GetMapping("/get")
    public String getContactMediumByOTP(@RequestParam String otp);

    @PostMapping("/put")
    public String putOtpAndContactMediumInCache(@RequestParam String otp, @RequestParam String contact_medium);
}
