package com.example.kadastr.integrational;

import com.example.kadastr.config.TestConfig;
import com.example.kadastr.dao.UserDAO;
import com.example.kadastr.dto.AuthRequest;
import com.example.kadastr.dto.RoleDto;
import com.example.kadastr.dto.UserDto;
import com.example.kadastr.exception.AuthException;
import com.example.kadastr.model.User;
import com.example.kadastr.security.util.AuthHelper;
import com.example.kadastr.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
public class UserServiceImplIntegrTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AuthHelper authHelper;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setName("Test");
        userDto.setSurname("User");
        userDto.setParentName("ParentName");
        userDto.setRole(new RoleDto("ROLE_ADMIN"));
    }

    @Test
    public void test_loadUserByUsername() {
        userService.createUser(userDto);
        Optional<User> loadUser = userDAO.findUserByUsername("testUser");
        assertTrue(loadUser.isPresent());
        assertEquals("testUser", loadUser.get().getUsername());
    }

    @Test
    public void test_createUser() {
        char[] generatedPassword = userService.createUser(userDto);
        Optional<User> optionalUser = userDAO.findUserByUsername("testUser");
        assertTrue(optionalUser.isPresent());

        assertNotNull(generatedPassword);
    }

    @Test
    public void test_getTokenValidCredentials() throws AuthException {
        char[] password = userService.createUser(userDto);
        AuthRequest authRequest = new AuthRequest("testUser", authHelper.restorePassword(password));
        String token = userService.getUserToken(authRequest);
        assertNotNull(token);
    }

    @Test
    public void test_getTokenInvalidCredentials() {
        userService.createUser(userDto);
        AuthRequest authRequest = new AuthRequest("testWrongUser", "wrong");
        assertThrows(AuthException.class, () -> {
            userService.getUserToken(authRequest);
        });
    }

}
