package org.schlunzis.kurtama.client.service;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import org.schlunzis.kurtama.client.events.ConnectionStatusEvent;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.LobbyInfo;

import java.util.Optional;

public interface ISessionService {

    Optional<IUser> getCurrentUser();

    ObservableList<LobbyInfo> getLobbyList();

    ObservableValue<ConnectionStatusEvent.Status> getConnectionStatus();

}
