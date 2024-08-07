package org.schlunzis.kurtama.client.fx.scene.events;

import org.schlunzis.kurtama.client.fx.scene.Scene;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record NewStageEvent(Scene scene, List<SceneChangeMessage> messages) {

    public NewStageEvent(Scene scene) {
        this(scene, Collections.emptyList());
    }

    public NewStageEvent(Scene scene, SceneChangeMessage... messages) {
        this(scene, Arrays.asList(messages));
    }

}
