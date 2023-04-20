package de.schlunzis.server.net.impl;

import de.schlunzis.server.net.Session;
import io.netty.channel.Channel;

import java.util.Collection;
import java.util.Optional;

public interface IChannelStore {

    void create(Channel channel);

    Optional<Channel> get(Session channelId);

    Optional<Session> get(Channel channel);

    void remove(Session channelId);

    void remove(Channel channel);

    Collection<Channel> getAll();

    Collection<Channel> get(Collection<Session> channelIds);

}
