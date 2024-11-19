package org.schlunzis.kurtama.client.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.client.fx.view.StatusView;

@Getter
@RequiredArgsConstructor
public enum RequirementsStatus implements StatusView.Status {
    TESTING("server.requirements.status.testing", StatusView.StatusType.PROGRESS),
    SUCCESS("server.requirements.status.met", StatusView.StatusType.SUCCESS),
    FAILED("server.requirements.status.notMet", StatusView.StatusType.ERROR);

    private final String i18nKey;
    private final StatusView.StatusType type;

}
