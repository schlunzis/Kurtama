package org.schlunzis.kurtama.common.game;

import org.schlunzis.kurtama.common.IUser;

import java.util.Collection;
import java.util.List;

public record GameSettings(int columns,
                           int rows,
                           List<Collection<IUser>> teams) {
}
