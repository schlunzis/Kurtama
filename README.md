# Kurtama

This repository contains the code for the first implementation of the board game Kurtama.

## Origin

After we finished our mandatory project in the course "Software Projekt" at the University of Oldenburg, we decided to
start from scratch with a new project. The goal is to create a playable version of the board game Kurtama. "What is
Kurtama?" you might ask. And it's a valid question. Chances are, you have never stumbled upon this game and this is for
good reason. Basically the game is not published and there is only one copy of the game in existence. The game was made
by some Environmental Sciences students at the University of Oldenburg. It tackles the topic of climate change and is
aimed to help, especially kids, understand what the reasons are for climate change and what we can do to stop more
devastating situations.

## Build

This project is written in Java 21 and uses Maven as its build tool. To build the project, you need to have Maven and
Java 21 installed. Clone this repository and run the following command in the root directory of the project:

```bash
mvn clean install package
```

This will build all modules of the project. To run the server, you can run the following command:

```bash
java -jar server/target/server-0.0.1-SNAPSHOT.jar
```

Make sure to not have the port `8007` in use, as this is the default port the server will listen on. You can change the
port by setting the environment variable `KURTAMA_SERVER_PORT` to your liking. Keep in mind that you will also have to
update the port in the client.

When the server is up and running, you can start as many clients as you want by running the following command:

```bash
java -jar client/target/client-0.0.1-SNAPSHOT.jar
```

Again, if you changed the port on the server you need to change the port on the client as well. You can do this by
setting
the environment variable `KURTAMA_SERVER_PORT` to the same value as on the server. If you are on the same machine, then
this might already be done with the server configuration.

## Further information

This is our first big project in Java completely written from scratch. We are still learning and trying to improve our
knowledge and skills. If you have any suggestions or feedback, feel free to contact us. We are always happy to learn
something new.
Also, we are in an early stage of development and there might come huge changes and rewrites to the codebase as we are
constantly finding new libraries and strategies that we want to adopt for our project. We hope, that one day, once the
base project is done and the dust is settled, the development will go much more rapidly, as we have a solid base to
build upon.