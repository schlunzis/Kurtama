package de.schlunzis.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:server.properties")
public class ServerConfig {
}
