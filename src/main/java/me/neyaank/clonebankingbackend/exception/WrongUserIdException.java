package me.neyaank.clonebankingbackend.exception;

public class WrongUserIdException extends RuntimeException {
    public WrongUserIdException(String exception) {
        super(exception);
    }
}
