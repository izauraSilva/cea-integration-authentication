package com.cea.jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedAuthenticationException extends RuntimeException {
    public UnauthorizedAuthenticationException(String message) {
        super(message);
    }
}
