package org.schlunzis.kurtama.server.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.game.GameSettings;
import org.schlunzis.kurtama.common.game.model.TeamColor;
import org.schlunzis.kurtama.server.auth.excptions.UnauthorizedRequestException;
import org.schlunzis.kurtama.server.game.model.Team;
import org.schlunzis.kurtama.server.lobby.LobbyManagement;
import org.schlunzis.kurtama.server.lobby.ServerLobby;
import org.schlunzis.kurtama.server.lobby.exception.LobbyNotFoundException;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.schlunzis.kurtama.server.user.exception.UserNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameManagement {

    private final GameStore gameStore;
    private final LobbyManagement lobbyManagement;

    public Game createGame(GameSettings gameSettings, UUID lobbyID, ServerUser requestingUser) throws LobbyNotFoundException, UserNotFoundException, UnauthorizedRequestException {
        ServerLobby lobby = lobbyManagement.getLobby(lobbyID);
        if (!lobby.getOwner().equals(requestingUser)) throw new UnauthorizedRequestException();
        List<Team> teams = createTeams(gameSettings, lobby);


        return gameStore.create(gameSettings, teams);
    }

    public void removeGame(Game game) {
        gameStore.remove(game.getId());
    }

    public void removeGame(UUID id) {
        gameStore.remove(id);
    }

    public Game getGame(UUID id) {
        return gameStore.get(id).orElseThrow();
    }

    private List<Team> createTeams(GameSettings gameSettings, ServerLobby lobby) throws UserNotFoundException {
        List<Team> teams = new ArrayList<>();
        List<Collection<IUser>> settingsTeams = gameSettings.teams();
        int colorCounter = 0;
        for (Collection<IUser> settingsTeam : settingsTeams) {
            List<ServerUser> teamUsers = new ArrayList<>(settingsTeam.size());
            // verify users are in lobby and get ServerUser
            for (IUser user : settingsTeam) {
                Optional<ServerUser> serverUser = lobby.getUsers().stream()
                        .filter(u -> u.getId().equals(user.getId()))
                        .findFirst();
                if (serverUser.isEmpty()) {
                    throw new UserNotFoundException();
                } else {
                    teamUsers.add(serverUser.get());
                }
            }

            //TODO generate random color with enough contrast to the other colors if there are no colors left
            if (colorCounter == TeamColor.values().length) {
                log.warn("Not enough colors for all teams. Reusing colors.");
                colorCounter = 0;
            }
            Team team = new Team(UUID.randomUUID(), TeamColor.values()[colorCounter++]);
            team.getUsers().addAll(teamUsers);
            teams.add(team);
        }
        return teams;
    }

}
