package org.schlunzis.kurtama.server.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.UserDTO;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerUser implements IUser {

    private UUID id;
    private String email;
    private String username;

    public UserDTO toDTO() {
        return new UserDTO(id, username);
    }

}
