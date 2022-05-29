package com.ivan.alkemybackendchallenge.security.exception.handler;

import com.ivan.alkemybackendchallenge.security.utility.CustomHttpServletResponseUtility;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        return CustomHttpServletResponseUtility.buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                this.extractRequestURI(request)
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request) {

        return CustomHttpServletResponseUtility.buildErrorResponse(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                ex.getMessage(),
                this.extractRequestURI(request)
        );
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
                                                        HttpHeaders headers,
                                                        HttpStatus status,
                                                        WebRequest request) {

        return CustomHttpServletResponseUtility.buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "param/arg type mismatch",
                this.extractRequestURI(request)
        );
    }

    private String extractRequestURI(WebRequest request) {
        return ((ServletWebRequest)request).getRequest().getRequestURI();
    }

}
