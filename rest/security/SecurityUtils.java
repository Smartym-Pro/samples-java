package pro.smartum.reptracker.gateway.security;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Sergey Valuy
 * 
 */
public final class SecurityUtils {

    private final static Logger log = LoggerFactory.getLogger(SecurityUtils.class);

    private SecurityUtils() {
    }

    @Nullable
    public static SecurityUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }
        if (authentication.getPrincipal() instanceof SecurityUser) {
            return ((SecurityUser) authentication.getPrincipal());
        }
        return null;
    }

}
