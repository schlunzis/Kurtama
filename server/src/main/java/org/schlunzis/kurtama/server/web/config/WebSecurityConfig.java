package org.schlunzis.kurtama.server.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

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


}