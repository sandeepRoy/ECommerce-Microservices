package com.msa.customer.clients;

import com.msa.customer.dtos.SMSRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "messagingClient", url = "http://localhost:8093/sms-service")
public interface MessegingClient {
    @PostMapping("/send")
    public ResponseEntity<String> sendSMS(@Validated @RequestBody SMSRequest smsRequest);
}
