package com.sandeep.sms.initializers;

import com.sandeep.sms.configurations.TwilioConfiguration;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Configuration
public class TwillioInitializer {

    private static final Logger logger = Logger.getLogger(TwillioInitializer.class.getName());

    private final TwilioConfiguration twilioConfiguration;

    @Autowired
    public TwillioInitializer(TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
        Twilio.init(
                twilioConfiguration.getAccount_sid(),
                twilioConfiguration.getAuth_token()
        );

        logger.info("Twillio Initialized with, Account-SID: " + twilioConfiguration.getAccount_sid() );
    }

}
