package org.schlunzis.kurtama.client.service;

import javafx.collections.ObservableList;
import org.schlunzis.kurtama.common.ILobby;
import org.schlunzis.kurtama.common.IUser;

import java.util.Optional;

public interface ISessionService {

    Optional<IUser> getCurrentUser();

    Optional<ILobby> getCurrentLobby();

    ObservableList<ILobby> getLobbyList();

}
