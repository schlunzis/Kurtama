package org.schlunzis.kurtama.server.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.game.GameSettings;
import org.schlunzis.kurtama.common.messages.game.client.MoveRequest;
import org.schlunzis.kurtama.common.messages.game.client.StartGameRequest;
import org.schlunzis.kurtama.common.messages.game.server.CouldNotCreateGameMessage;
import org.schlunzis.kurtama.common.messages.game.server.GameStartedMessage;
import org.schlunzis.kurtama.common.messages.game.server.UpdateGameStateMessage;
import org.schlunzis.kurtama.server.auth.excptions.UnauthorizedRequestException;
import org.schlunzis.kurtama.server.game.model.SquareGameState;
import org.schlunzis.kurtama.server.lobby.exception.LobbyNotFoundException;
import org.schlunzis.kurtama.server.service.ClientMessageContext;
import org.schlunzis.kurtama.server.service.ServerMessageWrappers;
import org.schlunzis.kurtama.server.user.exception.UserNotFoundException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameService {

    private final GameManagement gameManagement;

    @EventListener
    public ServerMessageWrappers onStartGameRequest(ClientMessageContext<StartGameRequest> cmc) {
        StartGameRequest request = cmc.getClientMessage();
        GameSettings gameSettings = request.gameSettings();
        log.info("Starting game with settings: {}", gameSettings);

        Game game;
        try {
            game = gameManagement.createGame(gameSettings, request.lobbyID(), cmc.getUser());
        } catch (LobbyNotFoundException | UserNotFoundException e) {
            log.info("Cannot create Game. Lobby not found.");
            cmc.respond(new CouldNotCreateGameMessage());
            return cmc.close();
        } catch (UnauthorizedRequestException e) {
            log.info("Cannot create Game. Unauthorized request.");
            cmc.respond(new CouldNotCreateGameMessage());
            return cmc.close();
        }
        SquareGameState gameState = game.getGameState();

        cmc.sendToMany(new GameStartedMessage(game.getId(), gameState.toDTO()), game.getAllUsers());
        return cmc.close();
    }

    @EventListener
    public ServerMessageWrappers onMoveRequest(ClientMessageContext<MoveRequest> cmc) {
        MoveRequest request = cmc.getClientMessage();
        log.info("Move request: {}", request);

        Game game = gameManagement.getGame(request.getGameID());
        game.move(cmc.getUser(), request.getFieldIndex());

        cmc.sendToMany(new UpdateGameStateMessage(game.getId(), game.getGameState().toDTO()), game.getAllUsers());
        return cmc.close();
    }

}
