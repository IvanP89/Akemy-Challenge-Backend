package com.ivan.alkemybackendchallenge.feature.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "api.documentation")
@Configuration
public class DocumentationConfig {

    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApiDocumentationUrl() {
        return url;
    }

}
