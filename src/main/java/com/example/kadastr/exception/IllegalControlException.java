package com.example.kadastr.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IllegalControlException extends Exception{

    private String message;

}
