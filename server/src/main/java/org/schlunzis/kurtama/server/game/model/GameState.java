package org.schlunzis.kurtama.server.game.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@RequiredArgsConstructor
public class GameState<D extends IDirection> {

    private final ITerrain<D> terrain;
    private final List<Team> teams;

}
