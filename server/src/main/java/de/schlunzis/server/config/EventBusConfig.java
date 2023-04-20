package de.schlunzis.server.config;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class EventBusConfig {

    private EventBus eventBus;

    @Bean
    public EventBus mainEventBus() {
        if (eventBus != null) return eventBus;

        eventBus = new EventBus("mainEventBus");
        eventBus.register(this);
        return eventBus;
    }

    @Subscribe
    public void onDeadEvent(DeadEvent deadEvent) {
        log.warn("Dead event: {}", deadEvent.getEvent());
    }

}
