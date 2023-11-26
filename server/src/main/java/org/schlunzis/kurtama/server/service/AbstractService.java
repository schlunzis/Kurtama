package org.schlunzis.kurtama.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

/**
 * A base class for all services, except the {@link org.schlunzis.kurtama.server.auth.AuthenticationService}.
 * <p>
 * This class provides basic functionality, for the services to send messages to users not sessions.
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractService {

    protected final ApplicationEventPublisher eventBus;

}
