package org.schlunzis.kurtama.server;

import org.schlunzis.kurtama.server.net.INetworkServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ServerApp {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ServerApp.class, args);
        context.getBean(INetworkServer.class).start();
    }

}
