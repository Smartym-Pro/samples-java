package pro.smartum.reptracker.gateway.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import pro.smartum.reptracker.gateway.dao.entities.UserRole;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Sergey Valuy
 * 
 */
public class SecurityUser extends User {

    private Long id;

    public SecurityUser(long userId, UserRole... roles) {
        super("fake-username", "fake-password", true, true, true, true, convert(roles));
        this.id = userId;
    }

    @NotNull
    private static Collection<? extends GrantedAuthority> convert(UserRole... roles) {
        List<GrantedAuthority> authorities = new LinkedList<GrantedAuthority>();
        for (UserRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
        }
        return authorities;
    }

    public Long getId() {
        return id;
    }
}
