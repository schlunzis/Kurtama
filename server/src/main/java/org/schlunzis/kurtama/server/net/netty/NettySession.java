package org.schlunzis.kurtama.server.net.netty;

import lombok.EqualsAndHashCode;
import org.schlunzis.kurtama.server.net.UUIDSession;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
public class NettySession extends UUIDSession {

    public NettySession(UUID uuid) {
        super(uuid);
    }

}
