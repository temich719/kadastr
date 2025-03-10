package com.example.kadastr.controller;

import com.example.kadastr.dto.AnswerMessageJson;
import com.example.kadastr.exception.InvalidInputDataException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

//parent class of all controllers that encapsulates response creation and validator check
@Controller
public class AbstractController {

    protected final ObjectProvider<AnswerMessageJson> answerMessageJson;
    private static final String SPACE = " ";

    @Autowired
    public AbstractController(ObjectProvider<AnswerMessageJson> answerMessageJson) {
        this.answerMessageJson = answerMessageJson;
    }

    //method builds http response answer
    protected AnswerMessageJson constructAnswer(String message, String status) {
        AnswerMessageJson ans = getAns();
        ans.setMessage(message);
        ans.setStatus(status);
        return ans;
    }

    //hibernate validator check input data
    protected void bindingResultCheck(BindingResult bindingResult) throws InvalidInputDataException {
        if (bindingResult.hasErrors()) {
            StringBuilder exceptionMessage = new StringBuilder();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                exceptionMessage.append(objectError.getDefaultMessage()).append(SPACE);
            }
            throw new InvalidInputDataException(exceptionMessage.toString());
        }
    }

    private AnswerMessageJson getAns() {
        return answerMessageJson.getObject();
    }

}
