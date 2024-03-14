package org.schlunzis.kurtama.server.server.model;

import org.junit.jupiter.api.Test;
import org.schlunzis.kurtama.common.game.GameSettings;
import org.schlunzis.kurtama.server.game.SquareTerrainFactory;
import org.schlunzis.kurtama.server.game.model.SquareTerrain;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SquareTerrainFactoryTest {


    @Test
    void createTerrainIndexTest() {
        GameSettings gameSettings = new GameSettings(2, 3);
        SquareTerrainFactory squareTerrainFactory = new SquareTerrainFactory(gameSettings);
        SquareTerrain terrain = (SquareTerrain) squareTerrainFactory.create();
        for (int i = 0; i < 6; i++) {
            assertEquals(i, terrain.get(i).getId());
        }
    }

}
