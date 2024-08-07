package org.schlunzis.kurtama.client.fx.scene;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.events.ClientReadyEvent;
import org.schlunzis.kurtama.client.fx.controller.MessageShowingController;
import org.schlunzis.kurtama.client.fx.scene.events.NewStageEvent;
import org.schlunzis.kurtama.client.fx.scene.events.SceneChangeEvent;
import org.schlunzis.kurtama.client.fx.scene.events.SceneChangeMessage;
import org.schlunzis.kurtama.client.util.I18n;
import org.schlunzis.kurtama.client.util.I18nBinder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

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
            FXMLLoader loader = createScene(event.scene());
            stage.setTitle("Kurtama - " + i18n.i18n("title." + event.scene().getTitleKey()));
            Scene scene = new Scene(loader.getRoot());
            stage.setScene(scene);
            stage.show();

            showMessages(loader, event.messages());
        });
    }

    @EventListener
    public void onNewStageEvent(NewStageEvent event) {
        log.debug("Opeing new stage with scene {}", event.scene());
        Platform.runLater(() -> {
            FXMLLoader loader = createScene(event.scene());
            Stage newStage = new Stage();
            newStage.setTitle("Kurtama - " + i18n.i18n("title." + event.scene().getTitleKey()));
            Scene scene = new Scene(loader.getRoot());
            newStage.setScene(scene);
            newStage.show();

            showMessages(loader, event.messages());
        });
    }

    private FXMLLoader createScene(org.schlunzis.kurtama.client.fx.scene.Scene scene) {
        FXMLLoader loader = new FXMLLoader(scene.getControllerClass().getResource(scene.getFxml()));
        loader.setControllerFactory(context::getBean);
        loader.setResources(i18n.getResourceBundle());
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            log.error("Error loading {}", scene.getFxml(), e);
        }
        if (parent == null) {
            throw new IllegalStateException("Could not load " + scene.getFxml()); // should never happen
        }

        i18nBinder.createBindings(parent);
        return loader;
    }

    private void showMessages(FXMLLoader loader, List<SceneChangeMessage> messages) {
        if (messages != null && !messages.isEmpty()) {
            Object controller = loader.getController();
            if (controller instanceof MessageShowingController msc)
                msc.showMessages(messages);
            else
                log.warn("{} does not implement MessageShowingController but there is a message to show", controller.getClass().getName());
        }
    }

}
