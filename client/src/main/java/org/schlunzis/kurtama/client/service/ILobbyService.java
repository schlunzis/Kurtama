package org.schlunzis.kurtama.client.service;

import javafx.collections.ObservableList;
import org.schlunzis.kurtama.common.ILobby;

import java.util.Optional;

public interface ILobbyService {

    Optional<ILobby> getCurrentLobby();

    ObservableList<String> getLobbyUsersList();

    void leaveLobby();

    void startGame();

}
