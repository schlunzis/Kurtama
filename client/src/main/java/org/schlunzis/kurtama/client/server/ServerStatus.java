package org.schlunzis.kurtama.client.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.client.fx.view.StatusView;

@Getter
@RequiredArgsConstructor
public enum ServerStatus implements StatusView.Status {
    NOT_STARTED("server.status.notStarted", StatusView.StatusType.INFO),
    DOWNLOADING("server.status.downloading", StatusView.StatusType.PROGRESS),
    DOWNLOAD_FAILED("server.status.download.failed", StatusView.StatusType.ERROR),
    STARTING("server.status.starting", StatusView.StatusType.PROGRESS),
    RUNNING("server.status.running", StatusView.StatusType.SUCCESS),
    RUN_FAILED("server.status.run.failed", StatusView.StatusType.ERROR),
    STOPPING("server.status.stopping", StatusView.StatusType.PROGRESS),
    STOPPING_FAILED("server.status.stopping.failed", StatusView.StatusType.ERROR),
    STOPPED("server.status.stopped", StatusView.StatusType.INFO);

    private final String i18nKey;
    private final StatusView.StatusType type;
}
