package com.sandeep.sms.services;

import com.sandeep.sms.configurations.TwilioConfiguration;
import com.sandeep.sms.requests.SMSRequest;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service("twillio")
public class SmsSenderImpl implements SmsSender {

    private static final Logger logger = Logger.getLogger(SmsSenderImpl.class.getName());

    private final TwilioConfiguration twilioConfiguration;

    @Autowired
    public SmsSenderImpl(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
    }

    @Override
    public void sendSMS(SMSRequest smsRequest) {
        if(isPhoneNumberValid(smsRequest.phone_number)) {
            PhoneNumber to = new PhoneNumber(smsRequest.getPhone_number());
            PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrial_number());
            String body = smsRequest.getMessage();

            MessageCreator creator = Message.creator(to, from, body); // txt message creator

            creator.create(); // sms sender

            logger.info("Sent SMS :  " + smsRequest);
        }
        else {
            throw new IllegalArgumentException("Phone number isn't valid!");
        }
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return true;
        // implement : Phone Number validation (Use Google Library)
    }
}
