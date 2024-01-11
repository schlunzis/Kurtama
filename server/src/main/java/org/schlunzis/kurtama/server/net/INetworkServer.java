package org.schlunzis.kurtama.server.net;

import org.schlunzis.kurtama.common.messages.IServerMessage;

import java.util.Collection;

public interface INetworkServer {

    void sendMessage(IServerMessage serverMessage);

    void sendMessage(IServerMessage serverMessage, Collection<ISession> recipients);

    void start();

}
