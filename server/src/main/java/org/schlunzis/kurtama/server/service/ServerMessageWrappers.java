package org.schlunzis.kurtama.server.service;

import java.util.Arrays;
import java.util.List;

public record ServerMessageWrappers(List<ServerMessageWrapper> wrappers) {

    public ServerMessageWrappers(ServerMessageWrapper... messages) {
        this(Arrays.asList(messages));
    }

}
