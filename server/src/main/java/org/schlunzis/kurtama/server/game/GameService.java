package org.schlunzis.kurtama.server.game;

import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.game.GameSettings;
import org.schlunzis.kurtama.common.messages.game.client.StartGameRequest;
import org.schlunzis.kurtama.common.messages.game.server.GameStartedMessage;
import org.schlunzis.kurtama.server.game.model.SquareGameState;
import org.schlunzis.kurtama.server.game.model.SquareTerrain;
import org.schlunzis.kurtama.server.service.ClientMessageContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
public class GameService {

    @EventListener
    public void onStartGameRequest(ClientMessageContext<StartGameRequest> cmc) {
        StartGameRequest request = cmc.getClientMessage();
        GameSettings gameSettings = request.gameSettings();
        log.info("Starting game with settings: {}", gameSettings);

        SquareTerrainFactory squareTerrainFactory = new SquareTerrainFactory(gameSettings);
        SquareTerrain terrain = (SquareTerrain) squareTerrainFactory.create();
        SquareGameState gameState = new SquareGameState(terrain, Collections.emptyList());

        cmc.respond(new GameStartedMessage(gameState.toDTO()));
        cmc.close();
    }

}
