package org.schlunzis.kurtama.client.service;

import org.schlunzis.kurtama.common.ILobby;

import java.util.Optional;

public interface ILobbyService {

    Optional<ILobby> getCurrentLobby();

    void leaveLobby();

}
