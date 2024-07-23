package org.schlunzis.kurtama.client.service;

import javafx.collections.ObservableList;
import org.schlunzis.kurtama.common.ILobby;
import org.schlunzis.kurtama.common.IUser;

import java.util.Optional;

public interface ILobbyService {

    Optional<ILobby> getCurrentLobby();

    ObservableList<IUser> getLobbyUsersList();

    void leaveLobby();

    void startGame();

}
