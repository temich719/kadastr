package com.example.kadastr.controller;

import com.example.kadastr.dto.AnswerMessageJson;
import com.example.kadastr.exception.InvalidInputDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@Controller
public class AbstractController {

    protected final AnswerMessageJson answerMessageJson;

    @Autowired
    public AbstractController(AnswerMessageJson answerMessageJson) {
        this.answerMessageJson = answerMessageJson;
    }

    protected void bindingResultCheck(BindingResult bindingResult) throws InvalidInputDataException {
        if (bindingResult.hasErrors()) {
            StringBuilder exceptionMessage = new StringBuilder();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                exceptionMessage.append(objectError.getDefaultMessage()).append(" ");
            }
            throw new InvalidInputDataException(exceptionMessage.toString());
        }
    }

}
