package de.schlunzis.server.user;

import de.schlunzis.common.IUser;
import de.schlunzis.common.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class ServerUser implements IUser {

    @Id
    @Column
    @GeneratedValue
    private UUID uuid;
    @Column
    private String email;
    @Column
    private String username;
    @Column
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
