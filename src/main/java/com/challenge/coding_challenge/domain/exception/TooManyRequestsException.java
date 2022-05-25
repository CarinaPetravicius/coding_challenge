package com.challenge.coding_challenge.domain.exception;

public class TooManyRequestsException extends RuntimeException {

    public TooManyRequestsException() {
        super("Too many requests");
    }

}