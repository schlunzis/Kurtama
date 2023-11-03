package org.schlunzis.kurtama.server;

import org.schlunzis.kurtama.server.net.INetworkServer;
import org.schlunzis.kurtama.server.user.DBUser;
import org.schlunzis.kurtama.server.user.IUserStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ServerApp {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ServerApp.class, args);


        //TODO remove this
        IUserStore userStore = context.getBean(IUserStore.class);
        PasswordEncoder pe = context.getBean(PasswordEncoder.class);
        for (int i = 0; i < 10; i++) {
            userStore.createUser(new DBUser("test" + i + "@test.de", "test" + i, pe.encode("test" + i)));
        }
        context.getBean(INetworkServer.class).start();
    }

}
