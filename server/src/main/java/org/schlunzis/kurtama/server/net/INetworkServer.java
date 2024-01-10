package org.schlunzis.kurtama.server.net;

import org.schlunzis.kurtama.common.messages.IServerMessage;

import java.util.Collection;

public interface INetworkServer<S extends ISession> {

    void sendMessage(IServerMessage serverMessage);

    void sendMessage(IServerMessage serverMessage, Collection<S> recipients);

    void start();

}
