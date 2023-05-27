package me.neyaank.clonebankingbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOwnerException extends RuntimeException {
    public InvalidOwnerException(String exception) {
        super(exception);
    }
}
