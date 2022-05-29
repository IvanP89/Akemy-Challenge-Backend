package com.ivan.alkemybackendchallenge.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EntityIdentifierNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityIdentifierNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public EntityIdentifierNotFoundException(String s) {
        super(s);
    }

}
