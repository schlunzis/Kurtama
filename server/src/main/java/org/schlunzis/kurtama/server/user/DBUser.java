package org.schlunzis.kurtama.server.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.UserDTO;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = DBUser.TABLE_NAME)
@Entity(name = DBUser.TABLE_NAME)
public class DBUser implements IUser {

    public static final String TABLE_NAME = "USERS";

    @Id
    @Column
    @GeneratedValue
    private UUID id;
    @Column
    private String email;
    @Column
    private String username;
    @Column
    private String passwordHash;

    /**
     * @param email        the email
     * @param username     the username
     * @param passwordHash the password hash
     */
    public DBUser(String email, String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    public ServerUser toServerUser() {
        return new ServerUser(id, email, username);
    }

    @Override
    public UserDTO toDTO() {
        return new UserDTO(id, username);
    }

}
