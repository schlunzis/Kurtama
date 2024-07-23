package org.schlunzis.kurtama.client.events;

public record ConnectionStatusEvent(Status status) {

    public enum Status {
        NOT_CONNECTED,
        CONNECTED,
        CONNECTING,
        FAILED
    }

}
