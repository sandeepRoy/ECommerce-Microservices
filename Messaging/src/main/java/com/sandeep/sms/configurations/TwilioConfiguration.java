package com.sandeep.sms.configurations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("twillio")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwilioConfiguration {
    private String account_sid;
    private String auth_token;
    private String trial_number;
}
