package com.example.kadastr.unit;

import com.example.kadastr.dao.RoleDAO;
import com.example.kadastr.dao.UserDAO;
import com.example.kadastr.dto.AuthRequest;
import com.example.kadastr.dto.RoleDto;
import com.example.kadastr.dto.UserDto;
import com.example.kadastr.exception.AuthException;
import com.example.kadastr.model.User;
import com.example.kadastr.security.JwtProvider;
import com.example.kadastr.security.util.AuthHelper;
import com.example.kadastr.security.util.PasswordGenerator;
import com.example.kadastr.service.impl.UserServiceImpl;
import com.example.kadastr.util.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private RoleDAO roleDAO;

    @Mock
    private Mapper<User, UserDto> userMapper;

    @Mock
    private AuthHelper authHelper;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordGenerator passwordGenerator;

    @InjectMocks
    private UserServiceImpl userService;

    private User mockUser;
    private UserDto userDto;
    private AuthRequest authRequest;

    @BeforeEach
    public void setUp() {
        mockUser = new User();
        mockUser.setUsername("testUser");

        String password = "password";
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        mockUser.setPassword(hashedPassword);

        authRequest = new AuthRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("password");

        userDto = new UserDto();
        userDto.setUsername("newUser");
        userDto.setRole(new RoleDto("ROLE_ADMIN"));
    }

    @Test
    public void testLoadUserByUsername_Success() {
        when(userDAO.findUserByUsername("testUser")).thenReturn(Optional.of(mockUser));

        var userDetails = userService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userDAO.findUserByUsername("testUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("testUser"));
    }

    @Test
    public void testCreateUser() {
        when(passwordGenerator.generatePassword()).thenReturn("password".toCharArray());
        when(roleDAO.findRoleByName("ROLE_ADMIN")).thenReturn(null);

        User mockUser = new User();

        when(userMapper.mapToModel(userDto)).thenReturn(mockUser);
        when(authHelper.restorePassword(any(char[].class))).thenReturn("password");
        when(userDAO.save(any(User.class))).thenReturn(mockUser);

        char[] password = userService.createUser(userDto);
        verify(userDAO, times(1)).save(any(User.class));
        assertNotNull(password);
    }

    @Test
    public void testGetUserToken_Success() throws AuthException {
        when(userDAO.findUserByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(jwtProvider.generateToken("testUser")).thenReturn("jwtToken");
        String token = userService.getUserToken(authRequest);
        assertEquals("jwtToken", token);
    }

    @Test
    public void testGetUserToken_Failed() {
        when(userDAO.findUserByUsername("testUser")).thenReturn(Optional.empty());
        assertThrows(AuthException.class, () -> userService.getUserToken(authRequest));
    }

}
