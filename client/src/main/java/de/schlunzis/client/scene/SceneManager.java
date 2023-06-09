package de.schlunzis.client.scene;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.schlunzis.client.scene.events.SceneChangeEvent;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;

@Slf4j
@Component
public class SceneManager {

    private final ApplicationContext context;

    @Setter
    private Stage stage;

    public SceneManager(ApplicationContext context, EventBus eventBus) {
        this.context = context;
        eventBus.register(this);
    }


    @Subscribe
    public void onSceneChangeMessage(SceneChangeEvent event) {
        log.debug("Changing scene to {}", event.scene());
        Platform.runLater(() -> {
            String path = "";

            switch (event.scene()) {
                case LOGIN -> {
                    path = "/fxml/login.fxml";
                    stage.setTitle("Login");
                }
                case CHAT -> {
                    path = "/fxml/chat.fxml";
                    stage.setTitle("Chat");
                }
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            loader.setControllerFactory(context::getBean);

            try {
                Parent root = loader.load();
                javafx.scene.Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error while loading scene: " + e.getMessage());
            }
        });
    }

}
