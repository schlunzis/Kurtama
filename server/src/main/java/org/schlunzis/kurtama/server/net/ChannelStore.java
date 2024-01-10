package org.schlunzis.kurtama.server.net;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class ChannelStore<S extends ISession, C> {

    private final Map<S, C> channelMap = new HashMap<>();
    private final Function<UUID, S> sessionConstructor;

    public S create(C channel) {
        UUID channelId = UUID.randomUUID();
        S session = sessionConstructor.apply(channelId);
        channelMap.put(session, channel);
        return session;
    }

    public Optional<C> get(S session) {
        return Optional.ofNullable(channelMap.get(session));
    }

    public Optional<S> get(C channel) {
        return channelMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(channel))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public void remove(S session) {
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

    public Collection<C> get(Collection<S> sessions) {
        List<C> result = new ArrayList<>();
        for (S session : sessions) {
            Optional<C> channel = get(session);
            channel.ifPresent(result::add);
        }
        return result;
    }

}
