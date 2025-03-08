package com.example.kadastr.security.util;

import com.example.kadastr.exception.AuthException;
import com.example.kadastr.exception.IllegalControlException;
import com.example.kadastr.model.ControllableEntity;
import com.example.kadastr.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.util.Objects.nonNull;

@Component
public class AuthHelper {

    public <T extends ControllableEntity> boolean checkUserControlOnEntity(T t) throws IllegalControlException {
        Authentication authentication = getAuthentication();
        if (nonNull(authentication) && authentication.isAuthenticated()) {
            User currentAuthenticatedUser = (User) authentication.getPrincipal();
            if (t.getEntityControllerUuid().equals(currentAuthenticatedUser.getUuid())
                    || isCurrentAuthenticatedUserAdmin(currentAuthenticatedUser)) {
                return true;
            }
        }
        throw new IllegalControlException("You haven't rights to manipulate with this entity");
    }

    public String restorePassword(char[] password) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : password) {
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public UUID getCurrentUserUUID() throws AuthException {
        Authentication authentication = getAuthentication();
        if (nonNull(authentication) && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            return user.getUuid();
        }
        throw new AuthException("Nobody authenticated!");
    }

    public boolean isCurrentAuthenticatedUserAdmin(User currentAuthenticatedUser) {
        return currentAuthenticatedUser.getRole().getAuthority().equals("ROLE_ADMIN");
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
