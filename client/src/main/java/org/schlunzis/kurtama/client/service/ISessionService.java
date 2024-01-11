package org.schlunzis.kurtama.client.service;

import javafx.collections.ObservableList;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.LobbyInfo;

import java.util.Optional;

public interface ISessionService {

    Optional<IUser> getCurrentUser();

    ObservableList<LobbyInfo> getLobbyList();

}
