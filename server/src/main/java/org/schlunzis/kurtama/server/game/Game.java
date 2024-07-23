package org.schlunzis.kurtama.server.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.server.game.model.SquareGameState;
import org.schlunzis.kurtama.server.game.model.SquareTile;
import org.schlunzis.kurtama.server.game.model.Team;
import org.schlunzis.kurtama.server.user.ServerUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
@Getter
@RequiredArgsConstructor
public class Game {

    private final UUID id;
    private final SquareGameState gameState;

    private final List<Team> teams = new ArrayList<>();

    public void move(ServerUser user, int fieldIndex) {
        Team team = getTeam(user);
        SquareTile currentTile = gameState.findTileWithFigureOfTeam(team);
        currentTile.getFigures().removeIf(figure -> figure.equals(team));
        gameState.getTerrain().get(fieldIndex).getFigures().add(team);
    }

    private Team getTeam(ServerUser user) {
        return teams.stream()
                .filter(team -> team.getUsers().contains(user))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User is not in a team"));
    }

    public Collection<ServerUser> getAllUsers() {
        return teams.stream()
                .flatMap(team -> team.getUsers().stream())
                .toList();
    }
}
