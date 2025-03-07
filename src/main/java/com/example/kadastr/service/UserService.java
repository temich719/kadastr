package com.example.kadastr.service;

import com.example.kadastr.dto.AuthRequest;
import com.example.kadastr.dto.UserDto;
import com.example.kadastr.exception.AuthException;

public interface UserService {

    void createUser(UserDto userDto);

    String getUserToken(AuthRequest authRequest) throws AuthException;

}
