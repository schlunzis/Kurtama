package org.schlunzis.kurtama.server.user;

import org.schlunzis.kurtama.common.IUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserStore {

    UUID createUser(DBUser user);

    Optional<DBUser> getUser(UUID uuid);

    Optional<DBUser> getUserByUserName(String username);

    Optional<DBUser> getUser(String email);

    boolean deleteUser(IUser User);

    boolean deleteUser(UUID id);

    List<IUser> getAllUsers();

}
