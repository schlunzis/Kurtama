package de.schlunzis.server.user;

import de.schlunzis.common.IUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class DBUser implements IUser {

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

    public ServerUser toServerUser() {
        return new ServerUser(uuid, email, username);
    }
}
