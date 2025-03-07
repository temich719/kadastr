package com.example.kadastr.service.impl;

import com.example.kadastr.dao.UserDAO;
import com.example.kadastr.dto.AuthRequest;
import com.example.kadastr.dto.UserDto;
import com.example.kadastr.exception.AuthException;
import com.example.kadastr.model.User;
import com.example.kadastr.security.JwtProvider;
import com.example.kadastr.security.util.PasswordGenerator;
import com.example.kadastr.service.UserService;
import com.example.kadastr.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserDAO userDAO;
    private final Mapper<User, UserDto> userMapper;
    private final PasswordGenerator passwordGenerator;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, Mapper<User, UserDto> userMapper, PasswordGenerator passwordGenerator, JwtProvider jwtProvider) {
        this.userDAO = userDAO;
        this.userMapper = userMapper;
        this.passwordGenerator = passwordGenerator;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.findUserByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User with username = " + username + " was not found")
        );
    }

    @Override
    public void createUser(UserDto userDto) {
        char[] password = passwordGenerator.generatePassword();
        byte[] bytePassword = getBytePassword(password);
        userDto.setPassword(BCrypt.hashpw(bytePassword, BCrypt.gensalt()).toCharArray());
        userDAO.save(userMapper.mapToModel(userDto));
        Arrays.fill(password, '\0');
        Arrays.fill(bytePassword, (byte) 0);
    }

    @Override
    public String getUserToken(AuthRequest authRequest) throws AuthException {
        Optional<User> optionalUser = userDAO.findUserByUsername(authRequest.getUsername());
        if (optionalUser.isPresent()
                && BCrypt.checkpw(getBytePassword(authRequest.getPassword()), optionalUser.get().getPassword())) {
            return jwtProvider.generateToken(authRequest.getUsername());
        } else {
            throw new AuthException("Invalid credentials or you haven't been registered yet");
        }
    }

    private byte[] getBytePassword(char[] password) {
        byte[] bytePassword = new byte[password.length];
        for (int i = 0; i < password.length; i++) {
            bytePassword[i] = (byte) password[i];
        }
        return bytePassword;
    }
}
