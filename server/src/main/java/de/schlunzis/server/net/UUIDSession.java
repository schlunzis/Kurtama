package de.schlunzis.server.net;

import lombok.Getter;

import java.util.UUID;

public class UUIDSession implements Session {

    @Getter
    private final UUID uuid;

    public UUIDSession(UUID uuid) {
        this.uuid = uuid;
    }

}
