package org.schlunzis.kurtama.server.game;

import org.schlunzis.kurtama.common.game.GameSettings;
import org.schlunzis.kurtama.server.game.model.Team;

import java.util.List;
import java.util.UUID;

public interface IGameFactory {
    Game create(UUID uuid, GameSettings gameSettings, List<Team> teams);
}
