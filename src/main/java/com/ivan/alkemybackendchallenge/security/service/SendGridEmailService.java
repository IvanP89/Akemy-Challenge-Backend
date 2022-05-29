package com.ivan.alkemybackendchallenge.security.service;

import com.ivan.alkemybackendchallenge.feature.configuration.DocumentationConfig;
import com.ivan.alkemybackendchallenge.security.configuration.EmailConfig;
import com.ivan.alkemybackendchallenge.security.dto.data.HttpResponseDto;
import com.ivan.alkemybackendchallenge.security.exception.EmailException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridEmailService implements EmailService {

    private final EmailConfig emailConfig;
    private final DocumentationConfig documentationConfig;

    @Autowired
    public SendGridEmailService(EmailConfig emailConfig, DocumentationConfig documentationConfig) {
        this.emailConfig = emailConfig;
        this.documentationConfig = documentationConfig;
    }

    @Override
    public HttpResponseDto sendEmail(String toAddress, String subject, String emailBody) throws EmailException {
        SendGrid sendGridClient = new SendGrid( this.emailConfig.getApiKey() );
        String emailRequestBody = this.buildSendGridRequestBody(toAddress, subject, emailBody);
        Request request = this.buildRequest(emailRequestBody);
        Response response;
        try {
            response = sendGridClient.api(request);
        } catch (IOException e) {
            throw new EmailException(e.getMessage(), e);
        }
        return new HttpResponseDto(
                response.getStatusCode(),
                response.getBody(),
                response.getHeaders()
        );
    }

    @Override
    public HttpResponseDto sendWelcomeEmail(String toAddress) throws EmailException {
        String subject = "Welcome to AlkemyDisneyAPI!";
        String emailBody = "Your registration to the API was successful.\n\n"
                + "Now you can use the provided email address to log into the API through the /auth/login endpoint.\n\n"
                + "For more information, check the API documentation at "
                + this.documentationConfig.getApiDocumentationUrl();
        return this.sendEmail(toAddress, subject, emailBody);
    }

    private String buildSendGridRequestBody(String toAddress, String subject, String emailBody) throws EmailException {
        Email from = new Email( this.emailConfig.getFromAddress() );
        Email to = new Email(toAddress);
        Content content = new Content("text/plain", emailBody);
        Mail mail = new Mail(from, subject, to, content);
        String requestBody;
        try {
            requestBody = mail.build();
        }catch (IOException e) {
            throw new EmailException(e.getMessage(), e);
        }
        return requestBody;
    }

    private Request buildRequest(String emailRequestBody) {
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(emailRequestBody);
        return request;
    }

}
