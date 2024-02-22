package org.schlunzis.kurtama.common.game.model;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GameState {

    private final ITerrain terrain;
    private final List<Team> teams;

}
