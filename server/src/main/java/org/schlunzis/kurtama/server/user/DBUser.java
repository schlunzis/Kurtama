package org.schlunzis.kurtama.server.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.schlunzis.kurtama.common.IUser;
import org.schlunzis.kurtama.common.Role;
import org.schlunzis.kurtama.common.UserDTO;

import java.util.Collection;
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

    // TODO: correctly store roles in the database. A look in the database currently shows that the roles leave a real mess.
    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Collection<Role> roles;

    /**
     * @param email        the email
     * @param username     the username
     * @param passwordHash the password hash
     */
    public DBUser(String email, String username, String passwordHash, Collection<Role> roles) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.roles = roles;
    }

    public ServerUser toServerUser() {
        return new ServerUser(id, email, username, roles);
    }

    @Override
    public UserDTO toDTO() {
        return new UserDTO(id, username, roles);
    }

}
