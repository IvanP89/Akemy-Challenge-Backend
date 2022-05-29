package com.ivan.alkemybackendchallenge.security.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "api.email.sendgrid")
@Configuration
public class EmailConfig {

    private String apiKey;
    private String fromAddress;

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getFromAddress() {
        return fromAddress;
    }

}
