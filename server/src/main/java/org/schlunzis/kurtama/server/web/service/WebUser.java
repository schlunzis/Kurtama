package org.schlunzis.kurtama.server.web.service;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.UUID;

/**
 * Spring does not provide a field for the user's id. We need to extend the User class to add this field.
 */
@Getter
public class WebUser extends User {

    private final UUID id;

    public WebUser(User user, UUID id) {
        super(user.getUsername(), user.getPassword(), user.getAuthorities());
        this.id = id;
    }
}