package org.schlunzis.kurtama.server.game.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.game.model.TeamColor;
import org.schlunzis.kurtama.common.game.model.TeamDTO;
import org.schlunzis.kurtama.server.user.ServerUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Team {

    private final UUID id;
    private final TeamColor color;
    private final List<ServerUser> users = new ArrayList<>();

    @Setter
    private int streetsLeft;
    @Setter
    private int housesLeft;

    public TeamDTO toDTO() {
        return new TeamDTO(id, color, users.stream().<IUser>map(IUser::toDTO).toList(), streetsLeft, housesLeft);
    }

}
