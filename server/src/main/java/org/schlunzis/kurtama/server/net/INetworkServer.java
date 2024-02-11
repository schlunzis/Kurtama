package org.schlunzis.kurtama.server.net;

import org.schlunzis.kurtama.common.messages.IServerMessage;

import java.util.Collection;

public interface INetworkServer {

    /**
     * Sends the message to all clients connected to the server.
     *
     * @param serverMessage the message to send
     */
    void sendMessage(IServerMessage serverMessage);

    /**
     * Sends the given message to all given recipients. Implementations can assume that all given sessions have the
     * type they were registered for by the {@link NetworkServerInitializer}.
     *
     * @param serverMessage the message to send
     * @param recipients    the sessions of the recipients
     */
    void sendMessage(IServerMessage serverMessage, Collection<ISession> recipients);

    /**
     * This method is called by the {@link NetworkServerInitializer}. The implementation may block the thread
     * indefinitely, or return immediately.
     */
    void start();

}
