package org.schlunzis.kurtama.client.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.ClientLauncher;
import org.schlunzis.kurtama.client.events.ClientClosingEvent;
import org.schlunzis.kurtama.client.events.ClientReadyEvent;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@Slf4j
public class ClientApp extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        this.context = new SpringApplicationBuilder()
                .sources(ClientLauncher.class)
                .run(getParameters().getRaw().toArray(new String[0]));
    }


    @Override
    public void start(Stage primaryStage) {
        //CSSFX.start();
        context.publishEvent(new ClientReadyEvent(primaryStage));
        setupIcons(primaryStage);
    }

    @Override
    public void stop() {
        // to whom it may concern: application is closing
        context.publishEvent(new ClientClosingEvent());
        this.context.close();
        Platform.exit();
    }

    private void setupIcons(Stage primaryStage) {
        // set icon on the application bar
        primaryStage.getIcons().addAll(
                getIconWithSize(16),
                getIconWithSize(32),
                getIconWithSize(64),
                getIconWithSize(128),
                getIconWithSize(256),
                getIconWithSize(512)
        );

        // set icon on the taskbar/dock
        if (Taskbar.isTaskbarSupported()) {
            final Taskbar taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                final java.awt.Image dockIcon = defaultToolkit.getImage(getClass().getResource("/icons/icon-512.png"));
                taskbar.setIconImage(dockIcon);
            }
        }
    }

    private Image getIconWithSize(int size) {
        return new Image(String.valueOf(getClass().getResource("/icons/icon-" + size + ".png")));
    }

}
