package org.schlunzis.kurtama.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LobbyDTO implements ILobby {

    private UUID id;
    private String name;
    private Collection<IUser> users;

    public LobbyDTO(UUID uuid, String name) {
        this.id = uuid;
        this.name = name;
    }

}


