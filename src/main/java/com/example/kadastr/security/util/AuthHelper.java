package com.example.kadastr.security.util;

import com.example.kadastr.exception.AuthException;
import com.example.kadastr.exception.IllegalControlException;
import com.example.kadastr.model.ControllableEntity;
import com.example.kadastr.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.example.kadastr.util.StringsStorage.ROLE_ADMIN;
import static java.util.Objects.nonNull;

@Component
public class AuthHelper {

    private static final String NOT_ENOUGH_CONTROL_RIGHTS = "You haven't rights to manipulate with this entity";
    private static final String NOBODY_AUTHENTICATED = "Nobody authenticated!";

    //checks if input object can be manipulated by currently authenticated user
    public <T extends ControllableEntity> boolean checkUserControlOnEntity(T t) throws IllegalControlException {
        Authentication authentication = getAuthentication();
        if (nonNull(authentication) && authentication.isAuthenticated()) {
            User currentAuthenticatedUser = (User) authentication.getPrincipal();
            if (t.getEntityControllerUuid().equals(currentAuthenticatedUser.getUuid())
                    || isCurrentAuthenticatedUserAdmin(currentAuthenticatedUser)) {
                return true;
            }
        }
        throw new IllegalControlException(NOT_ENOUGH_CONTROL_RIGHTS);
    }

    //convert char password to string
    public String restorePassword(char[] password) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : password) {
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    //gets current user uuid
    public UUID getCurrentUserUUID() throws AuthException {
        Authentication authentication = getAuthentication();
        if (nonNull(authentication) && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            return user.getUuid();
        }
        throw new AuthException(NOBODY_AUTHENTICATED);
    }

    //checks if current user has Admin rights
    public boolean isCurrentAuthenticatedUserAdmin(User currentAuthenticatedUser) {
        return currentAuthenticatedUser.getRole().getAuthority().equals(ROLE_ADMIN);
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
