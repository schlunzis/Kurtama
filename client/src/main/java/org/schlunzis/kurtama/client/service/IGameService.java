package org.schlunzis.kurtama.client.service;

import javafx.beans.property.ObjectProperty;
import org.schlunzis.kurtama.common.game.model.IGameStateDTO;

public interface IGameService {

    ObjectProperty<IGameStateDTO> getGameState();

    void sendMoveRequest(int id);
}

