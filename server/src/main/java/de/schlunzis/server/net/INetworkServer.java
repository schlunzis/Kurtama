package de.schlunzis.server.net;

import de.schlunzis.common.messages.IServerMessage;

import java.util.Collection;

public interface INetworkServer {

    void sendMessage(IServerMessage serverMessage);

    void sendMessage(IServerMessage serverMessage, Collection<ISession> recipients);

    void start();

}
