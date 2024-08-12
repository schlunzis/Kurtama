package org.schlunzis.kurtama.client.fx.scene.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static org.schlunzis.kurtama.client.fx.scene.events.SceneChangeEventType.ERROR;
import static org.schlunzis.kurtama.client.fx.scene.events.SceneChangeEventType.SUCCESS;

@Getter
@RequiredArgsConstructor
public enum SceneChangeMessage {

    USER_DELETED_SUCCESSFULLY("sceneChange.user.deleted.successfully", SUCCESS),
    CONNECTION_LOST("sceneChange.connection.lost", ERROR);

    private final String messageKey;
    private final SceneChangeEventType type;

}
