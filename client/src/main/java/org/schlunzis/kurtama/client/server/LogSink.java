package org.schlunzis.kurtama.client.server;

import lombok.Locked;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Setter
@Component
@NoArgsConstructor
public class LogSink {

    private Consumer<String> logConsumer;

    @Locked
    public void log(String message) {
        if (logConsumer != null) {
            logConsumer.accept(message);
        }
    }

}
