package org.schlunzis.kurtama.server.net.impl;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.net.UUIDSession;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class ChannelStore implements IChannelStore {

    private final Map<ISession, Channel> channelMap = new HashMap<>();

    @Override
    public void create(Channel channel) {
        UUID channelId;
        boolean match;
        do {
            channelId = UUID.randomUUID();
            final UUID finalID = channelId;
            match = channelMap.keySet().stream().anyMatch(session -> session.getId().equals(finalID));
        } while (match);
        channelMap.put(new UUIDSession(channelId), channel);
    }

    @Override
    public Optional<Channel> get(ISession session) {
        return Optional.ofNullable(channelMap.get(session));
    }

    @Override
    public Optional<ISession> get(Channel channel) {
        return channelMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(channel))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    @Override
    public void remove(ISession session) {
        channelMap.remove(session);
    }

    @Override
    public void remove(Channel channel) {
        channelMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(channel))
                .map(Map.Entry::getKey)
                .findFirst()
                .ifPresent(this::remove);
    }

    @Override
    public Collection<Channel> getAll() {
        return channelMap.values();
    }

    @Override
    public Collection<Channel> get(Collection<ISession> sessions) {
        List<Channel> result = new ArrayList<>();
        for (ISession session : sessions) {
            Optional<Channel> channel = get(session);
            channel.ifPresent(result::add);
        }
        return result;
    }

}
