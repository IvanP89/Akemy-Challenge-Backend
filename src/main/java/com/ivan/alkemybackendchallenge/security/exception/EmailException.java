package com.ivan.alkemybackendchallenge.security.exception;

import java.io.IOException;

public class EmailException extends IOException {
    public EmailException() {
    }

    public EmailException(String s) {
        super(s);
    }

    public EmailException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public EmailException(Throwable throwable) {
        super(throwable);
    }
}
