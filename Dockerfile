FROM maven:3.9.6-eclipse-temurin-22-alpine AS builder
WORKDIR /opt/app
COPY . .
RUN  mvn --activate-profiles docker --projects server --also-make --batch-mode --update-snapshots install package

FROM eclipse-temurin:22-jre-alpine
WORKDIR /opt/kurtama

# Create a group and user
RUN addgroup -S kurtama && \
    adduser -S -G kurtama kurtama && \
    chown -R kurtama:kurtama /opt/kurtama

COPY --from=builder /opt/app/server/target/server.jar .

# Change to 'kurtama' user
USER kurtama

ENTRYPOINT ["java", "-jar", "/opt/kurtama/server.jar"]