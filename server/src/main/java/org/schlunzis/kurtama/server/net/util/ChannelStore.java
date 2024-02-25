package org.schlunzis.kurtama.server.net.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.net.SessionType;
import org.schlunzis.kurtama.server.net.UUIDSession;
import org.schlunzis.kurtama.server.util.BiMap;
import org.schlunzis.kurtama.server.util.ConcurrentBiHashMap;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class ChannelStore<C> {

    private final BiMap<ISession, C> channelMap = new ConcurrentBiHashMap<>();
    private final SessionType sessionType;

    public ISession create(C channel) {
        final UUID channelId = UUID.randomUUID();
        final ISession session = new UUIDSession(channelId, sessionType);
        channelMap.put(session, channel);
        return session;
    }

    public Optional<C> get(ISession session) {
        return Optional.ofNullable(channelMap.get(session));
    }

    public Optional<ISession> get(C channel) {
        return Optional.ofNullable(channelMap.getByValue(channel));
    }

    public void remove(ISession session) {
        channelMap.remove(session);
    }

    public void remove(C channel) {
        channelMap.removeByValue(channel);
    }

    public Collection<C> getAll() {
        return channelMap.values();
    }

    public Collection<C> get(Collection<ISession> sessions) {
        return channelMap.getForKeys(sessions);
    }

}
