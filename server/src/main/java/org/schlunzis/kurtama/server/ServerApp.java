package org.schlunzis.kurtama.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.schlunzis.kurtama.server.net.NetworkServerInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
public class ServerApp {

    private static final Options OPTIONS = new Options();

    static {
        OPTIONS.addOption("h", "help", false, "Print this message");
        OPTIONS.addOption("v", "version", false, "Print version information");
    }

    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(OPTIONS, args);
        } catch (UnrecognizedOptionException e) {
            log.error("Unrecognized option: {}", e.getOption());
            System.exit(1);
        } catch (Exception e) {
            log.error("Error parsing command line arguments", e);
            System.exit(1);
        }

        boolean exitBeforeSpring = false;
        if (cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar kurtama-server.jar", OPTIONS);
            exitBeforeSpring = true;
        }
        if (cmd.hasOption("v")) {
            System.out.println(ServerApp.class.getPackage().getImplementationVersion());
            exitBeforeSpring = true;
        }

        if (exitBeforeSpring)
            System.exit(0);
        ApplicationContext context = SpringApplication.run(ServerApp.class, args);
        context.getBean(NetworkServerInitializer.class).init();
    }

}
