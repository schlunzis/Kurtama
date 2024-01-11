package org.schlunzis.kurtama.server.net;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class ChannelStore<C> {

    private final Map<ISession, C> channelMap = new HashMap<>();
    private final SessionType sessionType;

    public ISession create(C channel) {
        UUID channelId = UUID.randomUUID();
        ISession session = new UUIDSession(channelId, sessionType);
        channelMap.put(session, channel);
        return session;
    }

    public Optional<C> get(ISession session) {
        return Optional.ofNullable(channelMap.get(session));
    }

    public Optional<ISession> get(C channel) {
        return channelMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(channel))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public void remove(ISession session) {
        channelMap.remove(session);
    }

    public void remove(C channel) {
        channelMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(channel))
                .map(Map.Entry::getKey)
                .findFirst()
                .ifPresent(this::remove);
    }

    public Collection<C> getAll() {
        return channelMap.values();
    }

    public Collection<C> get(Collection<ISession> sessions) {
        List<C> result = new ArrayList<>();
        for (ISession session : sessions) {
            Optional<C> channel = get(session);
            channel.ifPresent(result::add);
        }
        return result;
    }

}
