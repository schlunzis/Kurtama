package org.schlunzis.kurtama.server.internal;

import org.schlunzis.kurtama.server.net.ISession;

public record ForcedLogoutEvent(ISession session) {
}
