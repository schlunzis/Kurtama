package org.schlunzis.kurtama.server.net;

import java.util.UUID;

public record UUIDSession(UUID id, SessionType sessionType) implements ISession {
}
