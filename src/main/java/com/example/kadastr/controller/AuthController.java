package com.example.kadastr.controller;

import com.example.kadastr.dto.AnswerMessageJson;
import com.example.kadastr.dto.AuthRequest;
import com.example.kadastr.dto.AuthResponse;
import com.example.kadastr.dto.UserDto;
import com.example.kadastr.exception.AuthException;
import com.example.kadastr.exception.InvalidInputDataException;
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

@RestController
@RequestMapping("/api/auth")
public class AuthController extends AbstractController {

    private final UserService userService;

    @Autowired
    public AuthController(ObjectProvider<AnswerMessageJson> answerMessageJson, UserService userService) {
        super(answerMessageJson);
        this.userService = userService;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AuthResponse login(HttpServletRequest request) throws InvalidInputDataException, AuthException {
        AuthRequest authRequest = getSecureAuthRequest(request);
        return new AuthResponse(userService.getUserToken(authRequest));
    }
//    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public AuthResponse login(@Valid @RequestBody AuthRequest authRequest, BindingResult bindingResult) throws InvalidInputDataException {
//        bindingResultCheck(bindingResult);
//        char[] password = authRequest.getPassword().toCharArray();
//
//        return null;
//    }

    @PostMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerMessageJson registration(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) throws InvalidInputDataException {
        bindingResultCheck(bindingResult);
        userService.createUser(userDto);
        return constructAnswer("Registration was okay", "REGISTERED");
    }

    private AuthRequest getSecureAuthRequest(HttpServletRequest request) throws InvalidInputDataException {
        StringBuilder loginBuilder = new StringBuilder();
        char[] password = new char[80];
        try (InputStream is = request.getInputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            int i = 0;
            while ((bytesRead = is.read()) != -1) {
                buffer[i] = (byte) bytesRead;
                i++;
            }

            for (int j = 0; j < i; j++) {
                char ch = (char) buffer[j];
                if (ch == 'l' && j + 4 < i
                        && (char) buffer[j + 1] == 'o'
                        && (char) buffer[j + 2] == 'g'
                        && (char) buffer[j + 3] == 'i'
                        && (char) buffer[j + 4] == 'n'
                ) {
                    j += 4;
                    while ((char) buffer[j] != '"') {
                        j++;
                    }
                    j++;
                    while ((char) buffer[j] != '"') {
                        j++;
                    }
                    j++;
                    while ((char) buffer[j] != '"') {
                        loginBuilder.append((char) buffer[j]);
                        j++;
                    }
                    j++;
                    ch = (char) buffer[j];
                }
                if (ch == 'p' && j + 7 < i
                        && (char) buffer[j + 1] == 'a'
                        && (char) buffer[j + 2] == 's'
                        && (char) buffer[j + 3] == 's'
                        && (char) buffer[j + 4] == 'w'
                        && (char) buffer[j + 5] == 'o'
                        && (char) buffer[j + 6] == 'r'
                        && (char) buffer[j + 7] == 'd'
                ) {
                    j += 7;
                    while ((char) buffer[j] != '"') {
                        j++;
                    }
                    j++;
                    while ((char) buffer[j] != '"') {
                        j++;
                    }
                    j++;
                    int passIndex = 0;
                    while ((char) buffer[j] != '"') {
                        password[passIndex] = (char) buffer[j];
                        j++;
                        passIndex++;
                    }
                    j++;
                }
            }
            return new AuthRequest(loginBuilder.toString(), password);
        } catch (IOException e) {
            throw new InvalidInputDataException(e.getMessage());
        }
    }
}
