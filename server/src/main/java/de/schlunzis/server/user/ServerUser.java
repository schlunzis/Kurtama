package de.schlunzis.server.user;

import de.schlunzis.common.IUser;
import de.schlunzis.common.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerUser implements IUser {

    private UUID uuid;
    private String email;
    private String username;

    public UserDTO toDTO() {
        return new UserDTO(uuid, username, "");
    }

}
