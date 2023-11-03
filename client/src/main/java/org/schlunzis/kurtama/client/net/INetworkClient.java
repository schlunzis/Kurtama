package org.schlunzis.kurtama.client.net;

import org.schlunzis.kurtama.common.messages.IClientMessage;

/**
 * The network client interface. Provides a layer between the application and the network layer.
 *
 * @since 1.0
 */
public interface INetworkClient {

    /**
     * Sends a message to the server
     *
     * @param clientMessage the message to send
     * @since 1.0
     */
    void sendMessage(IClientMessage clientMessage);

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
