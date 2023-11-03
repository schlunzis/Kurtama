package org.schlunzis.kurtama.client.fx.scene;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import org.schlunzis.kurtama.client.events.ClientReadyEvent;
import org.schlunzis.kurtama.client.fx.scene.events.SceneChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SceneManager {

    private final FxWeaver fxWeaver;
    private Stage stage;

    @EventListener
    void onClientReadyEvent(ClientReadyEvent cre) {
        stage = cre.stage();
        onSceneChangeMessage(new SceneChangeEvent(org.schlunzis.kurtama.client.fx.scene.Scene.LOGIN));
    }

    @EventListener
    public void onSceneChangeMessage(SceneChangeEvent event) {
        log.debug("Changing scene to {}", event.scene());
        Platform.runLater(() -> {
            // TODO: read the correct title string for each the current locale
            stage.setTitle(event.scene().getTitleKey());
            Scene scene = new Scene(fxWeaver.loadView(event.scene().getControllerClass()));
            stage.setScene(scene);
            stage.show();
        });
    }

}
