package com.sandeep.sms.services;

import com.sandeep.sms.requests.SMSRequest;

public interface SmsSender {
    public void sendSMS(SMSRequest smsRequest);
}
