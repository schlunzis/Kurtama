package org.schlunzis.kurtama.client.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.client.fx.view.StatusView;

public record ConnectionStatusEvent(Status status) {

    @Getter
    @RequiredArgsConstructor
    public enum Status implements StatusView.Status {
        NOT_CONNECTED("connection.status.not_connected", StatusView.StatusType.INFO),
        CONNECTED("connection.status.connected", StatusView.StatusType.SUCCESS),
        CONNECTING("connection.status.connecting", StatusView.StatusType.PROGRESS),
        FAILED("connection.status.failed", StatusView.StatusType.ERROR);

        private final String i18nKey;
        private final StatusView.StatusType type;
    }

}
