package org.schlunzis.kurtama.server.game.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.schlunzis.kurtama.common.IUser;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Team {

    private final UUID id;
    private final Color color;
    private final List<IUser> users = new ArrayList<>();

    @Setter
    private int streetsLeft;
    @Setter
    private int housesLeft;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
