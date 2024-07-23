package org.schlunzis.kurtama.client.fx.scene;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.events.ClientReadyEvent;
import org.schlunzis.kurtama.client.fx.scene.events.SceneChangeEvent;
import org.schlunzis.kurtama.client.util.I18n;
import org.schlunzis.kurtama.client.util.I18nBinder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class StageManager {

    private final ApplicationContext context;
    private final I18n i18n;
    private final I18nBinder i18nBinder;
    private Stage stage;

    @EventListener
    public void onClientReadyEvent(ClientReadyEvent cre) {
        stage = cre.stage();
        onSceneChangeMessage(new SceneChangeEvent(org.schlunzis.kurtama.client.fx.scene.Scene.LOGIN));
    }

    @EventListener
    public void onSceneChangeMessage(SceneChangeEvent event) {
        log.debug("Changing scene to {}", event.scene());
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(event.scene().getControllerClass().getResource(event.scene().getFxml()));
            loader.setControllerFactory(context::getBean);
            loader.setResources(i18n.getResourceBundle());
            Parent parent = null;
            try {
                parent = loader.load();
            } catch (IOException e) {
                log.error("Error loading {}", event.scene().getFxml(), e);
            }
            if (parent == null) {
                throw new IllegalStateException("Could not load " + event.scene().getFxml()); // should never happen
            }

            i18nBinder.createBindings(parent);
            stage.setTitle("Kurtama - " + i18n.i18n("title." + event.scene().getTitleKey()));
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        });
    }

}
