package com.ivan.alkemybackendchallenge.security.service;

import com.ivan.alkemybackendchallenge.security.dto.data.HttpResponseDto;
import com.ivan.alkemybackendchallenge.security.exception.EmailException;

public interface EmailService {

    HttpResponseDto sendEmail(String toAddress, String subject, String emailBody) throws EmailException;
    HttpResponseDto sendWelcomeEmail(String toAddress) throws EmailException;

}
