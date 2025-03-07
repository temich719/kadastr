package com.example.kadastr.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotRegisteredException extends Exception {

    private String message;

}
