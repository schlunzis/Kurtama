package org.schlunzis.kurtama.server.game;

import org.schlunzis.kurtama.common.game.GameSettings;

import java.util.UUID;

public interface IGameFactory {
    Game create(UUID uuid, GameSettings gameSettings);
}
