package pro.smartum.reptracker.gateway.web.controllers;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import pro.smartum.reptracker.gateway.security.SecurityUser;

/**
 * User: Sergey Valuy
 
 */
public final class TestSecurityUtils {

    private TestSecurityUtils() {
    }

    public static void authenticateUser(SecurityUser securityUser) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(securityUser,
                "", securityUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
