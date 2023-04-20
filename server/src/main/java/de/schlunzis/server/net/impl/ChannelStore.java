package de.schlunzis.server.net.impl;

import de.schlunzis.server.net.Session;
import de.schlunzis.server.net.UUIDSession;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class ChannelStore implements IChannelStore {

    private final Map<Session, Channel> channelMap = new HashMap<>();

    @Override
    public void create(Channel channel) {
        UUID channelId;
        do {
            channelId = UUID.randomUUID();
        } while (channelMap.containsKey(channelId)); //FIXME

        channelMap.put(new UUIDSession(channelId), channel);
    }

    @Override
    public Optional<Channel> get(Session session) {
        return Optional.ofNullable(channelMap.get(session));
    }

    @Override
    public Optional<Session> get(Channel channel) {
        return channelMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(channel))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    @Override
    public void remove(Session session) {
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
    public Collection<Channel> get(Collection<Session> sessions) {
        List<Channel> result = new ArrayList<>();
        for (Session session : sessions) {
            Optional<Channel> channel = get(session);
            channel.ifPresent(result::add);
        }
        return result;
    }

}
