package org.schlunzis.kurtama.client.fx.scene;

import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SceneFactory {

    private final FxWeaver fxWeaver;

    public javafx.scene.Scene create(Scene scene) {
        return new javafx.scene.Scene(fxWeaver.loadView(scene.getControllerClass()));
    }


}
