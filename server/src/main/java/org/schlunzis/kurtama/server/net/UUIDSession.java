package org.schlunzis.kurtama.server.net;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class UUIDSession implements ISession {

    private final UUID id;
    private final SessionType sessionType;

}
