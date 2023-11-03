package org.schlunzis.kurtama.server.net;

import lombok.Getter;

import java.util.UUID;

public class UUIDSession implements ISession {

    @Getter
    private final UUID id;

    public UUIDSession(UUID uuid) {
        this.id = uuid;
    }

}
