package org.schlunzis.kurtama.server.game.model;

import org.schlunzis.kurtama.common.game.model.ITerrainDTO;

public interface ITerrain {

    ITile get(int index);

    ITerrainDTO toDTO();

}
