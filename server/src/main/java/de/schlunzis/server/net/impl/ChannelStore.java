package de.schlunzis.server.net.impl;

import de.schlunzis.server.net.ISession;
import de.schlunzis.server.net.UUIDSession;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class ChannelStore implements IChannelStore {

    private final Map<ISession, Channel> channelMap = new HashMap<>();

    @Override
    public void create(Channel channel) {
        UUID channelId;
        do {
            channelId = UUID.randomUUID();
        } while (channelMap.containsKey(channelId)); //FIXME

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
