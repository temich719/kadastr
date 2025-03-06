package com.example.kadastr.exception;

import lombok.Getter;

@Getter
public class NoSuchIdException extends Exception {

    private final String message;

    public NoSuchIdException(String message) {
        this.message = message;
    }
}
