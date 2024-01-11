package org.schlunzis.kurtama.server.net;

import java.util.UUID;

public interface ISession {

    UUID getId();

    SessionType getSessionType();

}
