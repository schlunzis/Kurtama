package org.schlunzis.kurtama.server.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.game.GameSettings;
import org.schlunzis.kurtama.common.messages.game.client.MoveRequest;
import org.schlunzis.kurtama.common.messages.game.client.StartGameRequest;
import org.schlunzis.kurtama.common.messages.game.server.GameStartedMessage;
import org.schlunzis.kurtama.common.messages.game.server.UpdateGameStateMessage;
import org.schlunzis.kurtama.server.game.model.SquareGameState;
import org.schlunzis.kurtama.server.service.ClientMessageContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameService {

    private final GameManagement gameManagement;

    @EventListener
    public void onStartGameRequest(ClientMessageContext<StartGameRequest> cmc) {
        StartGameRequest request = cmc.getClientMessage();
        GameSettings gameSettings = request.gameSettings();
        log.info("Starting game with settings: {}", gameSettings);

        Game game = gameManagement.createGame(gameSettings);
        SquareGameState gameState = game.getGameState();

        cmc.respond(new GameStartedMessage(game.getId(), gameState.toDTO()));
        cmc.close();
    }

    @EventListener
    public void onMoveRequest(ClientMessageContext<MoveRequest> cmc) {
        MoveRequest request = cmc.getClientMessage();
        log.info("Move request: {}", request);

        Game game = gameManagement.getGame(request.getGameID());
        game.move(cmc.getUser(), request.getFieldIndex());

        cmc.respond(new UpdateGameStateMessage(game.getId(), game.getGameState().toDTO()));
        cmc.close();
    }

}
