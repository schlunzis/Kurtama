package de.schlunzis.client.scene;

import de.schlunzis.client.scene.events.ClientReadyEvent;
import de.schlunzis.client.scene.events.SceneChangeEvent;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.IOException;

import static de.schlunzis.client.scene.Scene.LOGIN;

@Slf4j
@Component
@RequiredArgsConstructor
public class SceneManager {

    private static final String LOGIN_FXML_PATH = "/fxml/login.fxml";
    private static final String MAIN_MENU_FXML_PATH = "/fxml/main.fxml";

    private final ApplicationContext context;

    @Setter
    private Stage stage;

    @EventListener
    void onClientReadyEvent(ClientReadyEvent cre) {
        stage = cre.stage();
        onSceneChangeMessage(new SceneChangeEvent(LOGIN));
    }

    @EventListener
    public void onSceneChangeMessage(SceneChangeEvent event) {
        log.debug("Changing scene to {}", event.scene());
        Platform.runLater(() -> {
            String path = "";

            switch (event.scene()) {
                case LOGIN -> {
                    path = LOGIN_FXML_PATH;
                    stage.setTitle("Login");
                }
                case MAIN -> {
                    path = MAIN_MENU_FXML_PATH;
                    stage.setTitle("Kurtama");
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
