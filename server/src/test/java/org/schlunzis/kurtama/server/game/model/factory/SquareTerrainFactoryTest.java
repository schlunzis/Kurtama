package org.schlunzis.kurtama.server.game.model.factory;

import org.junit.jupiter.api.Test;
import org.schlunzis.kurtama.common.game.GameSettings;
import org.schlunzis.kurtama.server.game.model.SquareTerrain;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SquareTerrainFactoryTest {

    @Test
    void createTerrainIndexTest() {
        GameSettings gameSettings = new GameSettings(2, 3, new ArrayList<>());
        SquareTerrainFactory squareTerrainFactory = new SquareTerrainFactory(gameSettings);
        SquareTerrain terrain = (SquareTerrain) squareTerrainFactory.create();
        for (int i = 0; i < 6; i++) {
            assertEquals(i, terrain.get(i).getId());
        }
    }

}
