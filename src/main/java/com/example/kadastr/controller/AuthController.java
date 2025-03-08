package com.example.kadastr.controller;

import com.example.kadastr.dto.*;
import com.example.kadastr.exception.AuthException;
import com.example.kadastr.exception.InvalidInputDataException;
import com.example.kadastr.security.util.AuthHelper;
import com.example.kadastr.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends AbstractController {

    private final UserService userService;
    private final AuthHelper authHelper;

    @Autowired
    public AuthController(ObjectProvider<AnswerMessageJson> answerMessageJson, UserService userService, AuthHelper authHelper) {
        super(answerMessageJson);
        this.userService = userService;
        this.authHelper = authHelper;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AuthResponse login(@Valid @RequestBody AuthRequest authRequest, BindingResult bindingResult) throws InvalidInputDataException, AuthException {
        bindingResultCheck(bindingResult);
        return new AuthResponse(userService.getUserToken(authRequest));
    }

    @PostMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public RegistrationResponse registration(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) throws InvalidInputDataException {
        bindingResultCheck(bindingResult);
        return new RegistrationResponse(authHelper.restorePassword(userService.createUser(userDto)));
    }

}
