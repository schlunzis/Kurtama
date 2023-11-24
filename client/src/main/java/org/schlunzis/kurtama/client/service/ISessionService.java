package org.schlunzis.kurtama.client.service;

import javafx.collections.ObservableList;
import org.schlunzis.kurtama.common.ILobby;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.LobbyInfo;

import java.util.Optional;
import java.util.UUID;

public interface ISessionService {

    Optional<IUser> getCurrentUser();

    Optional<ILobby> getCurrentLobby();

    ObservableList<LobbyInfo> getLobbyList();

    ObservableList<String> getChatMessages();

    UUID getCurrentChatID();

}
