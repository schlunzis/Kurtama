package de.schlunzis.server.net.impl;

import de.schlunzis.server.net.ISession;
import io.netty.channel.Channel;

import java.util.Collection;
import java.util.Optional;

public interface IChannelStore {

    void create(Channel channel);

    Optional<Channel> get(ISession channelId);

    Optional<ISession> get(Channel channel);

    void remove(ISession channelId);

    void remove(Channel channel);

    Collection<Channel> getAll();

    Collection<Channel> get(Collection<ISession> channelIds);

}
