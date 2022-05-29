package com.ivan.alkemybackendchallenge.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class EntityAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityAlreadyExistsException(String s) {
        super(s);
    }

    public EntityAlreadyExistsException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
