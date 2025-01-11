package com.sandeep.sms.services;

import com.sandeep.sms.requests.SMSRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TwillioSMSService {

    private final SmsSender smsSender;

    @Autowired
    public TwillioSMSService(@Qualifier("twillio") SmsSenderImpl smsSender) {
        this.smsSender = smsSender;
    }

    public void sendSMS(SMSRequest smsRequest) {
        smsSender.sendSMS(smsRequest);
    }
}
