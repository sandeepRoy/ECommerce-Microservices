package com.sandeep.sms.controllers;

import com.sandeep.sms.requests.SMSRequest;
import com.sandeep.sms.services.TwillioSMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms-service")
public class TwillioSMSController {

    private final TwillioSMSService twillioSMSService;

    @Autowired
    public TwillioSMSController(TwillioSMSService twillioSMSService) {
        this.twillioSMSService = twillioSMSService;
    }

    @PostMapping("/send")
    public void sendSMS(@Validated @RequestBody SMSRequest smsRequest) {
        twillioSMSService.sendSMS(smsRequest);
    }
}
