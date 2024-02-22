package org.schlunzis.kurtama.client.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.ClientLauncher;
import org.schlunzis.kurtama.client.events.ClientClosingEvent;
import org.schlunzis.kurtama.client.events.ClientReadyEvent;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

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
    }

    @Override
    public void stop() {
        // to whom it may concern: application is closing
        context.publishEvent(new ClientClosingEvent());
        this.context.close();
        Platform.exit();
    }
}
