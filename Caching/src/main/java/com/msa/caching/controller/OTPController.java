package com.msa.caching.controller;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cache")
public class OTPController {

    @PostMapping("/put")
    // RedisCache's Service annotation that puts up value for the specified key = "#otp" in given cache_name = otpCache
    // here return specifies the put() method
    @CachePut(value = "otpCache", key = "#otp")
    public String putOtpAndMobileInCache(@RequestParam String otp, @RequestParam String mobile) {
        // tells the redis cache to put value(mobile) in key(otp)
        return mobile;
    }

    @GetMapping("/get")
    // RedisCache's Service annotation that looks up for value for the specified key = "#otp" in given cache_name = otpCache
    @Cacheable(value = "otpCache", key = "#otp")
    public String getMobileByOTP(@RequestParam String otp) {
        // if value(mobile) found using key(otp), return the value, trigged using Configuration
        // else return not found HttpResponse
        String mobile = "OTP Not Found!";
        HttpStatus status = HttpStatus.NOT_FOUND;

        if (mobile.equals("OTP Not Found!")) {
            return status.getReasonPhrase();
        }

        return mobile;
    }
}
