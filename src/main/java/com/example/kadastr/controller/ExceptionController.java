package com.example.kadastr.controller;

import com.example.kadastr.dto.AnswerMessageJson;
import com.example.kadastr.exception.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ExceptionController extends AbstractController {

    private static final String ERROR_STATUS = "ERROR";
    private static final String UNKNOWN_ERROR = "UNKNOWN ERROR OCCURRED: ";

    public ExceptionController(ObjectProvider<AnswerMessageJson> answerMessageJson) {
        super(answerMessageJson);
    }

    @ExceptionHandler({AuthException.class, IllegalControlException.class, InvalidInputDataException.class, InvalidTokenException.class, NoSuchIdException.class, NotRegisteredException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AnswerMessageJson handleMyExceptions(Exception e) {
        return constructAnswer(e.getMessage(), ERROR_STATUS);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AnswerMessageJson handleThrowable(Exception e) {
        return constructAnswer(UNKNOWN_ERROR + e.getMessage(), ERROR_STATUS);
    }
}
