package org.schlunzis.kurtama.server.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.server.user.IUserStore;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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

    @Bean
    void developmentInitialization() {
        // creating some test users
        for (int i = 0; i < 10; i++) {
            UUID id = userStore.createUser(new ServerUser("test" + i + "@schlunzis.org", "test" + i, "test" + i));
            log.debug("Created user with id {}", id);
        }
    }

}
