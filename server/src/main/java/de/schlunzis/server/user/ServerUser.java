package de.schlunzis.server.user;

import de.schlunzis.common.IUser;
import de.schlunzis.common.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ServerUser implements IUser {

    private UUID uuid;
    private String email;
    private String username;
    private String password;

    public ServerUser(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }


    public UserDTO toDTO() {
        return new UserDTO(uuid, username, email);
    }

}
