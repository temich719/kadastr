package com.example.kadastr.exception;

import lombok.Getter;

@Getter
public class InvalidInputDataException extends Exception {

    private final String message;

    public InvalidInputDataException(String message) {
        this.message = message;
    }
}
