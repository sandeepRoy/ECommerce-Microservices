package com.msa.order.clients;

import com.msa.order.requests.sms.SMSRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "messeging", url = "http://localhost:8093/sms-service")
public interface MessegingClient {
    @PostMapping("/send")
    public ResponseEntity<String> sendSMS(@Validated @RequestBody SMSRequest smsRequest);
}
