package com.example.kadastr.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidInputDataException extends Exception {

    private String message;

}
