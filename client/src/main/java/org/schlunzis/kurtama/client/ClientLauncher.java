package org.schlunzis.kurtama.client;

import javafx.application.Application;
import org.schlunzis.kurtama.client.fx.ClientApp;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientLauncher {

    public static void main(String[] args) {
        Application.launch(ClientApp.class, args);
    }

}
