package org.schlunzis.kurtama.server.lobby;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.schlunzis.kurtama.common.ILobby;
import org.schlunzis.kurtama.common.LobbyDTO;
import org.schlunzis.kurtama.common.LobbyInfo;
import org.schlunzis.kurtama.server.user.ServerUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ServerLobby implements ILobby {

    private final UUID id;
    private final Collection<ServerUser> users = new ArrayList<>();
    @Setter
    private String name;
    @Setter
    private String passwordHash;
    @Setter
    private UUID chatID;
    @Setter
    private ServerUser owner;

    public LobbyDTO toDTO() {
        return new LobbyDTO(id, name, users.stream().map(ServerUser::toDTO).toList(), chatID, owner.toDTO());
    }

    void joinUser(ServerUser user) {
        users.add(user);
    }

    void leaveUser(ServerUser user) {
        users.removeIf(u -> u.getId().equals(user.getId()));
        if (owner.equals(user)) {
            owner = users.stream().findFirst().orElse(null); // can only be null if no users are left
        }
    }

    public LobbyInfo getInfo() {
        return new LobbyInfo(id, name, isPasswordProtected(), users.size());
    }

    public boolean isPasswordProtected() {
        return !passwordHash.isBlank();
    }
}
