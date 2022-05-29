package com.ivan.alkemybackendchallenge.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CustomIllegalArgumentException extends IllegalArgumentException {

    public CustomIllegalArgumentException() {
    }

    public CustomIllegalArgumentException(String s) {
        super(s);
    }

    public CustomIllegalArgumentException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CustomIllegalArgumentException(Throwable throwable) {
        super(throwable);
    }

}
