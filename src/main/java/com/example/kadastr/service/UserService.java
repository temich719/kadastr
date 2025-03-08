package com.example.kadastr.service;

import com.example.kadastr.dto.AuthRequest;
import com.example.kadastr.dto.UserDto;
import com.example.kadastr.exception.AuthException;

public interface UserService {

    /**
     * creates new user
     * @param userDto DTO of user that needs to be created
     * @return generated password
     */
    char[] createUser(UserDto userDto);

    /**
     * login method that generates personal JWT token
     * @param authRequest request that contains input login(username) and password
     * @return JWT token
     * @throws AuthException when given credentials are invalid, or user hasn't been registered yet
     */
    String getUserToken(AuthRequest authRequest) throws AuthException;

}
