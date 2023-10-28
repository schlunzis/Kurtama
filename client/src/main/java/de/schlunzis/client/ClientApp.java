package de.schlunzis.client;

import de.schlunzis.client.net.NetworkClient;
import de.schlunzis.client.net.NetworkStartThread;
import de.schlunzis.client.scene.Scene;
import de.schlunzis.client.scene.SceneManager;
import de.schlunzis.client.scene.events.SceneChangeEvent;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan
public class ClientApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("de.schlunzis.client");
        SceneManager sceneManager = context.getBean(SceneManager.class);
        sceneManager.setStage(stage);

        context.getBean(ApplicationEventPublisher.class).publishEvent(new SceneChangeEvent(Scene.LOGIN));

        context.getBean(NetworkStartThread.class).start();

        stage.setOnCloseRequest(event -> {
            context.getBean(NetworkClient.class).close();
            // TODO: initiate a logout (see Issue "Allow Logout of user #8")
            // maybe loosing the connection is enough?
            // also this whole thing could be moved to a separate service?
        });
    }
}
