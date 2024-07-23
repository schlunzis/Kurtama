package org.schlunzis.kurtama.common.game.model;

import org.schlunzis.kurtama.common.IUser;

import java.util.List;
import java.util.UUID;

public record TeamDTO(UUID id,
                      TeamColor color,
                      List<IUser> users,
                      int streetsLeft,
                      int housesLeft) {
}
