package org.schlunzis.kurtama.server.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.Role;
import org.schlunzis.kurtama.common.UserDTO;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ServerUser implements IUser {

    @EqualsAndHashCode.Include
    private UUID id;
    private String email;
    private String username;
    private Set<Role> roles;

    public UserDTO toDTO() {
        return new UserDTO(id, username, roles);
    }

}
