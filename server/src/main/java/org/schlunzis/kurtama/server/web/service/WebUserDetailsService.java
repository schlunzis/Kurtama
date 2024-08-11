package org.schlunzis.kurtama.server.web.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.server.user.DBUser;
import org.schlunzis.kurtama.server.user.UserStore;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * This class is used by Spring Security to authenticate users.
 * Instead of providing a predefined list of users, this class fetches the user from the database.
 * <p>
 * Currently, only a login via username is supported.
 * We might want to have a look at the email address as well. See <a href="https://stackoverflow.com/q/50673400">here</a>
 * for more information.
 */
@Service
@RequiredArgsConstructor
public class WebUserDetailsService implements UserDetailsService {

    private final UserStore userStore;

    /**
     * Spring does not provide a field for the user's id. We need to extend the User class to add this field.
     */
    @Getter
    public static class WebUser extends User {

        private final UUID id;

        public WebUser(User user, UUID id) {
            super(user.getUsername(), user.getPassword(), user.getAuthorities());
            this.id = id;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<DBUser> optUser = userStore.getUserByUserName(username);
        if (optUser.isPresent()) {
            DBUser user = optUser.get();
            User springUser = (User) User.builder()
                    .username(user.getUsername())
                    .password(user.getPasswordHash())
                    .roles(user.getRoles().stream().map(Enum::toString).toArray(String[]::new))
                    .build();
            return new WebUser(springUser, user.getId());
        }
        throw new UsernameNotFoundException("User not found");
    }

}
