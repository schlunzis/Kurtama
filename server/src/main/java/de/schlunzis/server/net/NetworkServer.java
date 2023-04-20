package de.schlunzis.server.net;

import de.schlunzis.common.messages.ServerMessage;

import java.util.Collection;

public interface NetworkServer {

    void sendMessage(ServerMessage serverMessage);

    void sendMessage(ServerMessage serverMessage, Collection<Session> recipients);

    void start();

}
