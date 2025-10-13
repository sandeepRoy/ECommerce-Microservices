package com.ecms.configserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationConfiguration {
    @Value("${DATABASE_USERNAME}")
    public String database_username;

    public String getDatabase_username() {
        return database_username;
    }

    public void setDatabase_username(String database_username) {
        this.database_username = database_username;
    }
}
