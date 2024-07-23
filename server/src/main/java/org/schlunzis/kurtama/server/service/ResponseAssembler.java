package org.schlunzis.kurtama.server.service;

import lombok.Getter;
import lombok.Locked;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.schlunzis.kurtama.common.messages.IClientMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@RequiredArgsConstructor
class ResponseAssembler {

    private final IClientMessage clientRequest;
    private final List<ServerMessageWrapper> additionalMessages = new ArrayList<>();
    private ServerMessageWrapper mainResponse = null;

    @Locked.Write
    void setMainResponse(ServerMessageWrapper message) {
        mainResponse = message;
    }

    @Locked.Write
    void addAdditionalMessage(ServerMessageWrapper message) {
        additionalMessages.add(message);
    }

    @Locked.Read
    Optional<ServerMessageWrapper> getMainResponse() {
        return Optional.ofNullable(mainResponse);
    }

    @Locked.Read
    List<ServerMessageWrapper> assemble() {
        List<ServerMessageWrapper> wrappers = new ArrayList<>();
        if (mainResponse != null)
            wrappers.add(mainResponse);
        wrappers.addAll(additionalMessages);
        return wrappers;
    }

}
