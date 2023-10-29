package de.schlunzis.server;

import de.schlunzis.server.net.INetworkServer;
import de.schlunzis.server.user.ServerUser;
import de.schlunzis.server.user.IUserStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ServerApp {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ServerApp.class, args);


        //TODO remove this
        IUserStore userStore = context.getBean(IUserStore.class);
        for (int i = 0; i < 10; i++) {
            userStore.createUser(new ServerUser("test" + i + "@test.de", "test" + i, "test" + i));
        }
        context.getBean(INetworkServer.class).start();
    }

}
