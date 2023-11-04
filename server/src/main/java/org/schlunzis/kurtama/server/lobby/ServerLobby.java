package org.schlunzis.kurtama.server.lobby;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.schlunzis.kurtama.common.ILobby;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.LobbyDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ServerLobby implements ILobby {

    private final UUID id;
    private final Collection<IUser> users = new ArrayList<>();
    private String name;

    public LobbyDTO toDTO() {
        return new LobbyDTO(id, name, users.stream().toList());
    }

    public void joinUser(IUser user) {
        users.add(user);
    }

    public void leaveUser(IUser user) {
        users.removeIf(u -> u.getId().equals(user.getId()));
    }

}
