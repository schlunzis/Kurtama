package org.schlunzis.kurtama.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class LobbyDTO implements ILobby {

    private final UUID id;
    private final String name;
    private final Collection<IUser> users;

    public LobbyDTO(UUID uuid, String name) {
        this.id = uuid;
        this.name = name;
        this.users = Collections.emptyList();
    }

}


