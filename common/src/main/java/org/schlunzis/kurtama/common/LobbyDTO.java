package org.schlunzis.kurtama.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LobbyDTO implements ILobby {

    private UUID id;
    private String name;
    private Collection<UserDTO> users;
    private UUID chatID;

}


