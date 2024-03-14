FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /opt/app
COPY . .
RUN  mvn --activate-profiles docker --projects server --also-make --batch-mode --update-snapshots install package

FROM openjdk:21
WORKDIR /opt/app

COPY --from=builder /opt/app/server/target/server.jar .

ENTRYPOINT ["java", "-jar", "/opt/app/server.jar"]