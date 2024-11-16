package org.schlunzis.kurtama.server.web.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.Role;
import org.schlunzis.kurtama.server.user.DBUser;
import org.schlunzis.kurtama.server.user.IUserStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Set;
import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final IUserStore userStore;
    private final PasswordEncoder pe;

    @Value("${kurtama.monitoring.password:pr0m3th3us}")
    private String monitoringPassword;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/monitoring/**").hasRole("MONITORING")
                                .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .csrf(AbstractHttpConfigurer::disable);
        // TODO: make use of csrf token properly (https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html)
        return http.build();

    }

    /**
     * Once the application is ready, create the monitoring user.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void createMonitoringUser() {
        UUID id = userStore.createUser(new DBUser("monitoring@schlunzis.org", "monitoring", pe.encode(monitoringPassword), Set.of(Role.MONITORING)));
        log.debug("Created monitoring user with id {}", id);
    }

}