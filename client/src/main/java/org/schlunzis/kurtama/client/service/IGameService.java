package org.schlunzis.kurtama.client.service;

import javafx.beans.property.ObjectProperty;
import org.schlunzis.kurtama.common.game.model.IGameStateDTO;

public interface IGameService {

    ObjectProperty<IGameStateDTO> getGameState();

    /**
     * Sends a move request to the server.
     *
     * @param id the id of the tile to move to
     */
    void sendMoveRequest(int id);

}

