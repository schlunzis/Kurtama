package org.schlunzis.kurtama.server.net;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public class UUIDSession implements ISession {

    private final UUID id;

    public UUIDSession(UUID uuid) {
        this.id = uuid;
    }

}
