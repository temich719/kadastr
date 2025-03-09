package com.example.kadastr.controller;

import com.example.kadastr.dto.*;
import com.example.kadastr.exception.AuthException;
import com.example.kadastr.exception.InvalidInputDataException;
import com.example.kadastr.security.util.AuthHelper;
import com.example.kadastr.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.example.kadastr.util.StringsStorage.*;

//controller for registration and login
@RestController
@RequestMapping(AUTH_CONTROLLER_URL)
public class AuthController extends AbstractController {

    private final UserService userService;
    private final AuthHelper authHelper;

    @Autowired
    public AuthController(ObjectProvider<AnswerMessageJson> answerMessageJson, UserService userService, AuthHelper authHelper) {
        super(answerMessageJson);
        this.userService = userService;
        this.authHelper = authHelper;
    }

    @PostMapping(value = AUTH_CONTROLLER_LOGIN_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AuthResponse login(@Valid @RequestBody AuthRequest authRequest, BindingResult bindingResult) throws InvalidInputDataException, AuthException {
        bindingResultCheck(bindingResult);
        return new AuthResponse(userService.getUserToken(authRequest));
    }

    @PostMapping(value = AUTH_CONTROLLER_REGISTRATION_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public RegistrationResponse registration(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) throws InvalidInputDataException {
        bindingResultCheck(bindingResult);
        return new RegistrationResponse(authHelper.restorePassword(userService.createUser(userDto)));
    }

}
