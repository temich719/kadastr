package com.example.kadastr.service.impl;

import com.example.kadastr.dao.RoleDAO;
import com.example.kadastr.dao.UserDAO;
import com.example.kadastr.dto.AuthRequest;
import com.example.kadastr.dto.UserDto;
import com.example.kadastr.exception.AuthException;
import com.example.kadastr.model.User;
import com.example.kadastr.security.JwtProvider;
import com.example.kadastr.security.util.AuthHelper;
import com.example.kadastr.security.util.PasswordGenerator;
import com.example.kadastr.service.UserService;
import com.example.kadastr.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final Mapper<User, UserDto> userMapper;
    private final PasswordGenerator passwordGenerator;
    private final JwtProvider jwtProvider;
    private final AuthHelper authHelper;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, RoleDAO roleDAO, Mapper<User, UserDto> userMapper, PasswordGenerator passwordGenerator, JwtProvider jwtProvider, AuthHelper authHelper) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.userMapper = userMapper;
        this.passwordGenerator = passwordGenerator;
        this.jwtProvider = jwtProvider;
        this.authHelper = authHelper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Cacheable(value = "usersCache", key = "#username")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.findUserByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User with username = " + username + " was not found")
        );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public char[] createUser(UserDto userDto) {
        char[] password = passwordGenerator.generatePassword();
        User userToCreate = userMapper.mapToModel(userDto);
        userToCreate.setRole(roleDAO.findRoleByName(userDto.getRole().getName()));
        userToCreate.setPassword(BCrypt.hashpw(authHelper.restorePassword(password), BCrypt.gensalt()));
        userDAO.save(userToCreate);
        return password;
    }

    @Transactional(rollbackFor = Exception.class)
    @Cacheable(value = "usersCache", key = "#authRequest")
    @Override
    public String getUserToken(AuthRequest authRequest) throws AuthException {
        Optional<User> optionalUser = userDAO.findUserByUsername(authRequest.getUsername());
        if (optionalUser.isPresent()
                && BCrypt.checkpw(authRequest.getPassword(), optionalUser.get().getPassword())) {
            return jwtProvider.generateToken(authRequest.getUsername());
        } else {
            throw new AuthException("Invalid credentials or you haven't been registered yet");
        }
    }

}
