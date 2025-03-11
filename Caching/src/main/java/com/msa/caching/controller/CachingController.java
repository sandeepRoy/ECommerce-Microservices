package com.msa.caching.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/cache")
public class CachingController {

    public HashMap<String, String> otpCache = new HashMap<>();

    @GetMapping("/get")
    public ResponseEntity<String> getMobileByOTP(@RequestParam String otp) {
        return new ResponseEntity<>(otpCache.get(otp), HttpStatus.OK);
    }

    @PostMapping("/put")
    public ResponseEntity<String> putOtpAndMobileInCache(@RequestParam String otp, @RequestParam String mobile) {
        otpCache.put(otp, mobile);
        return new ResponseEntity<>("Pushed!", HttpStatus.CREATED);
    }
}
