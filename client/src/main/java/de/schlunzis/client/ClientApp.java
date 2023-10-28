package de.schlunzis.client;

import de.schlunzis.client.net.NetworkClient;
import de.schlunzis.client.net.NetworkStartThread;
import de.schlunzis.client.scene.Scene;
import de.schlunzis.client.scene.SceneManager;
import de.schlunzis.client.scene.events.SceneChangeEvent;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

@Slf4j
public class ClientApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ApplicationContext context = SpringApplication.run(ClientApp.class);
        SceneManager sceneManager = context.getBean(SceneManager.class);
        sceneManager.setStage(stage);

        context.publishEvent(new SceneChangeEvent(Scene.LOGIN));

        context.getBean(NetworkStartThread.class).start();

        stage.setOnCloseRequest(event -> {
            context.getBean(NetworkClient.class).close();
            // TODO: this whole thing could be moved to a separate service?
        });
    }
}
