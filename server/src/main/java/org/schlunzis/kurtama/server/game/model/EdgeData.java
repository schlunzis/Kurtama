package org.schlunzis.kurtama.server.game.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.game.model.EdgeDTO;
import org.schlunzis.kurtama.server.user.ServerUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class EdgeData {

    private final int id;

    // these references are redundant, but are useful to create the DTO
    private final ITile first;
    private final IDirection firstDirection;
    private final ITile second;
    private final IDirection secondDirection;

    private final List<ServerUser> streets = new ArrayList<>();

    public EdgeDTO toDTO() {
        List<IUser> streetDTOs = this.streets.stream().<IUser>map(ServerUser::toDTO).toList();
        return new EdgeDTO(id, first.getId(), second.getId(), streetDTOs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeData edgeData = (EdgeData) o;
        return id == edgeData.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
