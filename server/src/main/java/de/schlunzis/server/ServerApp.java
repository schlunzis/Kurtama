package de.schlunzis.server;

import de.schlunzis.server.net.NetworkServer;
import de.schlunzis.server.user.ServerUser;
import de.schlunzis.server.user.UserStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ServerApp {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ServerApp.class, args);


        //TODO remove this
        UserStore userStore = context.getBean(UserStore.class);
        for (int i = 0; i < 10; i++) {
            userStore.createUser(new ServerUser("test" + i + "@test.de", "test" + i, "test" + i));
        }
        context.getBean(NetworkServer.class).start();
    }

}
