package de.schlunzis.client.net;

import de.schlunzis.common.messages.ClientMessage;

/**
 * The network client interface. Provides a layer between the application and the network layer.
 *
 * @since 1.0
 */
public interface NetworkClient {

    /**
     * Sends a message to the server
     *
     * @param clientMessage the message to send
     * @since 1.0
     */
    void sendMessage(ClientMessage clientMessage);

    /**
     * Starts the network client and initiates the connection to the server
     *
     * @since 1.0
     */
    void start();

    /**
     * Closes the connection to the server
     *
     * @since 1.0
     */
    void close();
}
