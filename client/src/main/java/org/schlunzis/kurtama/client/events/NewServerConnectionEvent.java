package org.schlunzis.kurtama.client.events;

public record NewServerConnectionEvent(String host, int port) {
}
