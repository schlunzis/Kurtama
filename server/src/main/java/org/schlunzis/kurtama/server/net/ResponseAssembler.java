package org.schlunzis.kurtama.server.net;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.schlunzis.kurtama.common.messages.IClientMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@RequiredArgsConstructor
public class ResponseAssembler {

    private final IClientMessage clientRequest;
    private final List<ServerMessageWrapper> additionalMessages = new ArrayList<>();
    @Getter(AccessLevel.NONE)
    private ServerMessageWrapper mainResponse = null;

    public void addAdditionalMessage(ServerMessageWrapper message) {
        additionalMessages.add(message);
    }

    public Optional<ServerMessageWrapper> getMainResponse() {
        return Optional.ofNullable(mainResponse);
    }

    public List<ServerMessageWrapper> assemble() {
        List<ServerMessageWrapper> wrappers = new ArrayList<>();
        if (mainResponse != null)
            wrappers.add(mainResponse);
        wrappers.addAll(additionalMessages);
        return wrappers;
    }

}
