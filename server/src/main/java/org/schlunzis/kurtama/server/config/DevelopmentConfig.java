package org.schlunzis.kurtama.server.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.server.user.DBUser;
import org.schlunzis.kurtama.server.user.IUserStore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

/**
 * Configuration class for development environment.
 * <p>
 * All configurations will only be applied if the profile "dev" is active.
 */
@Slf4j
@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DevelopmentConfig {

    private final IUserStore userStore;
    private final PasswordEncoder pe;

    @PostConstruct
    void developmentInitialization() {
        // creating some test users
        for (int i = 0; i < 10; i++) {
            UUID id = userStore.createUser(new DBUser("test" + i + "@schlunzis.org", "test" + i, pe.encode("test" + i)));
            log.debug("Created user with id {}", id);
        }
    }

}
