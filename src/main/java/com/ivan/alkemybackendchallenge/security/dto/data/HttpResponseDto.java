package com.ivan.alkemybackendchallenge.security.dto.data;

import java.util.Map;

public class HttpResponseDto {

    private int statusCode;
    private String body;
    private Map<String, String> headers;

    public HttpResponseDto(int statusCode, String responseBody, Map<String, String> responseHeaders) {
        this.statusCode = statusCode;
        this.body = responseBody;
        this.headers = responseHeaders;
    }

    public HttpResponseDto() {
        this.reset();
    }

    public void reset() {
        this.statusCode = 0;
        this.body = "";
        this.headers = null;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getBody() {
        return this.body;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public HttpResponseDto setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public HttpResponseDto setBody(String body) {
        this.body = body;
        return this;
    }

    public HttpResponseDto setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

}
