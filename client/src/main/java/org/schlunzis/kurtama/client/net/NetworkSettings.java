package org.schlunzis.kurtama.client.net;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class NetworkSettings {

    @Value("${kurtama.server.port}")
    private int port;
    @Value("${kurtama.server.host}")
    private String host;

}
